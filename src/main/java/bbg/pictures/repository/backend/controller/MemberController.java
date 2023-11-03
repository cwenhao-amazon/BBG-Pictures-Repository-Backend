package bbg.pictures.repository.backend.controller;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Member> saveMember(@RequestBody final Member member) {
        memberService.save(member);

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<Member>> getMembers() {
        final Iterable<Member> members = memberService.findAll();

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<Member> getMember(@PathVariable final String name) {
        final Member member = memberService.findByName(name);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PatchMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<String> updateMember(@PathVariable final String name, @RequestBody final Member member) {
        memberService.update(name, member);

        return new ResponseEntity<>("Successfully updated member with name: '" + name + "'", HttpStatus.OK);
    }

    @DeleteMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<String> deleteMember(@PathVariable final String name) {
        memberService.delete(name);

        //TODO Make it so an exception is thrown when the entity with the name does not exists. Currently it returns the same response as it does when it exists.
        return new ResponseEntity<>("Successfully deleted member with name: '" + name + "'", HttpStatus.OK);
    }
}
