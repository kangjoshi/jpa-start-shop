package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Member;
import edu.til.jpastartshop.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MemberService {

    MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(Member member) {
        if (memberRepository.existsById(member.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        return memberRepository.save(member);
    }

    public Member findById(long memberId) {
        if (memberId == 0) {
            throw new IllegalArgumentException("잘못된 형식의 회원 아이디입니다.");
        }

        return memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
    }
}
