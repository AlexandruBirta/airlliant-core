package ro.unibuc.fmi.airlliantcore.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.airlliantcore.repository.UserRepository;
import ro.unibuc.fmi.airlliantmodel.entity.User;
import ro.unibuc.fmi.airlliantmodel.exception.ApiException;
import ro.unibuc.fmi.airlliantmodel.exception.ExceptionStatus;


@Slf4j
@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    @Transactional
    public void createUser(User user) {

        String email = user.getEmail();

        if (userRepository.existsUserByEmail(email)) {
            throw new ApiException(ExceptionStatus.USER_ALREADY_EXISTS, email);
        }

        userRepository.save(user);
        log.info("Created user: {}", user);

    }

    @Transactional
    public void removeUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ApiException(ExceptionStatus.USER_NOT_FOUND, String.valueOf(id));
        }

        userRepository.deleteById(id);

        log.info("Deleted user with id '{}'.", id);

    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApiException(ExceptionStatus.USER_NOT_FOUND, String.valueOf(id)));
    }

}