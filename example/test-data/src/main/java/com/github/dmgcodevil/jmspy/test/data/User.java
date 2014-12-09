package com.github.dmgcodevil.jmspy.test.data;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Final class with default constructor.
 */
public final class User {

    private String login;
    private String password;
    private List<Role> roles;
    private Set<Account> accounts = Collections.emptySet();
    private List<Contact> contacts = Collections.emptyList();

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Set<Account> getAccounts() {
        return Collections.unmodifiableSet(accounts);
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Contact> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
