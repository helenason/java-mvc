package di.stage4.annotations;

import di.User;

@Service
class UserService {

    @Inject
    private UserDao userDao;

    public UserService() {
    }

    public User join(final User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
