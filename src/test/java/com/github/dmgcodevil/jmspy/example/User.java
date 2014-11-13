package com.github.dmgcodevil.jmspy.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class User extends BaseDomain {

    private String login;
    private String password;
    private Profile profile;
    private List<Role> roles = new ArrayList<>();
    Map<String, Account> accounts = new HashMap<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.put(account.getName(), account);
    }

    /**
     * Returns implementation version.
     *
     * @return version.
     */
    public int getImplVersion() {
        return 1;
    }
}
