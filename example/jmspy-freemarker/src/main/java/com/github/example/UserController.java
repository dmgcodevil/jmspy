package com.github.example;


import com.github.dmgcodevil.jmspy.MethodInvocationRecorder;
import com.github.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    /**
     * Static list of users to simulate Database
     */
    private static List<User> userList = new ArrayList<User>();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    private MethodInvocationRecorder methodInvocationRecorder;

    @Autowired
    public void setMethodInvocationRecorder(MethodInvocationRecorder methodInvocationRecorder) {
        this.methodInvocationRecorder = methodInvocationRecorder;
    }


    @PostConstruct
    private void init() {
        userList.add(newUser("Bill", "Gates"));
        userList.add(newUser("Steve", "Jobs"));
        userList.add(newUser("Larry", "Page"));
        userList.add(newUser("Sergey", "Brin"));
        userList.add(newUser("Larry", "Ellison"));
    }

    private User newUser(String f, String l) {
        return userRepository.createUser(f, l);
    }

    /**
     * Saves the static list of users in model and renders it
     * via freemarker template.
     *
     * @param model
     * @return The index view (FTL)
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@ModelAttribute("model") ModelMap model) {

        model.addAttribute("userList", userList);

        return "index";
    }

    /**
     * Add a new user into static user lists and display the
     * same into FTL via redirect
     *
     * @param user
     * @return Redirect to /index page to display user list
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("user") User user) {

        if (null != user && null != user.getFirstname()
                && null != user.getLastname() && !user.getFirstname().isEmpty()
                && !user.getLastname().isEmpty()) {

            synchronized (userList) {
                userList.add(userRepository.createUser(user.getFirstname(), user.getLastname()));
            }

        }

        return "redirect:index.html";
    }


    @RequestMapping(value = "/saveSnapshot", method = RequestMethod.GET)
    public String saveSnapshot() {
        methodInvocationRecorder.makeSnapshot();
        return "redirect:index.html";
    }
}