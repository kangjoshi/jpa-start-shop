package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Member;
import edu.til.jpastartshop.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTest {
    /*
    * 회원 등록
    * 회원 조회
    * */

    @MockBean
    MemberRepository memberRepository;

    MemberService memberService;

    @BeforeAll
    public void init() {
        memberService = new MemberService(memberRepository);
    }

    @Test
    public void givenMemberThenReturnCreatedMember() {
        Member member = new Member(1L, "kangjoshi");

        when(memberRepository.save(member)).thenReturn(member);

        Member created = memberService.create(member);

        assertNotNull(created);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    public void givenMemberWithExistedIdThenThrowsException() {
        Member member = new Member(1L, "john");

        when(memberService.create(member)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> memberService.create(member));
    }

    @Test
    public void givenMemberIdThenReturnMember() {
        long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member(1L, "kangjoshi")));

        Member member = memberService.findById(memberId);

        assertNotNull(member);
        verify(memberRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void givenWrongMemberIdThenThrowsException() {
        long memberId = 0;

        assertThrows(IllegalArgumentException.class, () -> memberService.findById(memberId));

        verify(memberRepository, never()).findById(any(Long.class));
    }

    @Test
    public void givenNotExistMemberIdThenThrowsException() {
        long memberId = 99L;

        assertThrows(NoSuchElementException.class, () -> memberService.findById(memberId));

        verify(memberRepository, times(1)).findById(any(Long.class));
    }

}