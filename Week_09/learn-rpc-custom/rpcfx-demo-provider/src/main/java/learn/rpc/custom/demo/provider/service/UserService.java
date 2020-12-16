package learn.rpc.custom.demo.provider.service;


import learn.rpc.custom.demo.api.domain.User;
import learn.rpc.custom.demo.api.service.IUserService;

public class UserService implements IUserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }

}
