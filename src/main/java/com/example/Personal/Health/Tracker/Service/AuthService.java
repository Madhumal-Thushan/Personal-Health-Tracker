package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Entity.UserPrincipal;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    /** Get Current User to Authenticate
     *
     * @param username
     * @return {@Value UserDetails}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Users user = userRepo.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            return new UserPrincipal(user);

        } catch (DataAccessException e) {
            throw new UsernameNotFoundException("Database error occurred while searching for user: " + username, e);

        } catch (Exception e) {
            throw new UsernameNotFoundException("An unexpected error occurred while trying to find user: " + username, e);
        }
    }
}
