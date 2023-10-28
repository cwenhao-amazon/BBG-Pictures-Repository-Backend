package bbg.pictures.repository.backend.controller;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.service.MemberService;
import bbg.pictures.repository.backend.validation.MemberRequestValidator;
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
    @Autowired
    private MemberRequestValidator validator;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<Member>> getMembers() {
        final Iterable<Member> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Member> saveMember(@RequestBody final Member member) {
        validator.validateOnPost(member);
        memberService.save(member);

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<Member> updateMember(@PathVariable final String name, @RequestBody final Member member) {
        validator.validateOnPatch(name, member);
        final Member updatedMember = memberService.update(name, member);

        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{name}", produces = "application/json")
    public ResponseEntity<String> deleteMember(@PathVariable final String name) {
        validator.validateOnDelete(name);
        memberService.delete(name);

        return new ResponseEntity<>("Successfully deleted member with name: '" + name + "'", HttpStatus.OK);
    }
}
