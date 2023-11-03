package bbg.pictures.repository.backend.controller;

import java.net.URI;
import java.time.LocalDateTime;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.model.response.SuccessResponse;
import bbg.pictures.repository.backend.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Member> saveMember(@RequestBody final Member member) {
        final Member createdMember = memberService.save(member);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                        .path("/{name}")
                                                        .buildAndExpand(createdMember.getName())
                                                        .toUri();
        return ResponseEntity.created(location).body(member);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<Member>> getMembers() {
        final Iterable<Member> members = memberService.findAll();

        return ResponseEntity.ok().body(members);
    }

    @GetMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<Member> getMember(@PathVariable final String name) {
        final Member member = memberService.findByName(name);

        return ResponseEntity.ok().body(member);
    }

    @PatchMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<SuccessResponse> updateMember(@PathVariable final String name, @RequestBody final Member member) {
        memberService.update(name, member);

        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Successfully updated member with name: '" + name + "'")
                                                            .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<SuccessResponse> deleteMember(@PathVariable final String name) {
        memberService.delete(name);

        //TODO Make it so an exception is thrown when the entity with the name does not exists. Currently it returns the same response as it does when it exists.
        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Successfully deleted member with name: '" + name + "'")
                                                            .build();
        return ResponseEntity.ok().body(responseBody);
    }
}
