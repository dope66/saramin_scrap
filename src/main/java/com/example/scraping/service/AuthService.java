package com.example.scraping.service;

import com.example.scraping.domain.user.User;
import com.example.scraping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new UserDetail(user.get());
        } else {
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 일치 하지않습니다.");
        }
    }
}
