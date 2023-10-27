package bbg.pictures.repository.backend.controller;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<Member>> getMembers() {
        final Iterable<Member> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Member> saveMembers(@RequestBody final Member member) {
        memberService.save(member);

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }
}
