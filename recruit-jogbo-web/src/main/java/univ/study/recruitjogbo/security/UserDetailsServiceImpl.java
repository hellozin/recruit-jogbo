package univ.study.recruitjogbo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.member.Role;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberService.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(memberId));

        Set<GrantedAuthority> authorities = new HashSet<>();
        if (member.isEmailConfirmed()) {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.value()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.UNCONFIRMED.value()));
        }

        return new AuthMember(member.getId(), member.getMemberId(), member.getPassword(), authorities);
    }

}
