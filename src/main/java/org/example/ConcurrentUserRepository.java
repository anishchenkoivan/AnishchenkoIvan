package org.example;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentUserRepository implements UserRepository {

    ConcurrentHashMap<String, User> userByMsisdn = new ConcurrentHashMap<>();

    @Override
    public User findByMsisdn(String msisdn) {
        return userByMsisdn.get(msisdn);
    }

    @Override
    public void updateUserByMsisdn(String msisdn, User user) {
        userByMsisdn.put(msisdn, user);
    }
}
