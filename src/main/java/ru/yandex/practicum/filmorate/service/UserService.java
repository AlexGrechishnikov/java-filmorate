package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public User createUser(User user) {
        checkUser(user);
        return userStorage.createUser(user);
    }

    private void checkUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(id);
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        user.getFriendsIds().remove(friendId);
        friend.getFriendsIds().remove(id);
    }

    public List<User> findUserFriends(Long id) {
        List<User> result = new ArrayList<>();
        Set<Long> friendsIds = findUserById(id).getFriendsIds();
        for (Long friendId : friendsIds) {
            result.add(findUserById(friendId));
        }
        return result;
    }

    public List<User> findUsersCommonFriends(Long id, Long otherId) {
        List<User> result = new ArrayList<>(findUserFriends(id));
        result.retainAll(findUserFriends(otherId));
        return result;
    }
}