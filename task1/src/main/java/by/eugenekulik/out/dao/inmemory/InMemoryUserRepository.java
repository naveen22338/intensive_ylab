package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.utils.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users;
    private final Sequence sequence;


    public InMemoryUserRepository(Sequence sequence) {
        users = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<User> findById(Long id) {
        int left = 0;
        int right = users.size() - 1;
        while (left < right - 1) {
            if (users.get((left + right) / 2).getId().equals(id)) {
                return Optional.of(users.get((left + right) / 2));
            }
            if (users.get((left + right) / 2).getId() < id) {
                left = (left + right) / 2;
            } else {
                right = (left + right) / 2;
            }
        }
        if (users.get(left).getId().equals(id)) {
            return Optional.of(users.get(left));
        }
        if (users.get(right).getId().equals(id)) {
            return Optional.of(users.get(right));
        }
        return Optional.empty();
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        user.setId(sequence.next());
        users.add(user);
        return user;
    }

    @Override
    public List<User> getPage(int page, int count) {
        return IntStream.range(0, users.size())
            .mapToObj(i -> users.get(i))
            .skip(count * page).limit(count).toList();
    }
}
