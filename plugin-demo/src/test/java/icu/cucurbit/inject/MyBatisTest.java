package icu.cucurbit.inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import icu.cucurbit.FilterContext;
import icu.cucurbit.inject.dao.UserDao;
import icu.cucurbit.inject.entity.User;
import icu.cucurbit.sql.TableRule;
import icu.cucurbit.sql.filter.FilterFactory;
import icu.cucurbit.sql.filter.RuleFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyBatisTest {

    @Autowired
    private UserDao userDao;

    @Before
    public void setup() {
        List<TableRule> rules = Lists.newArrayList(
                new TableRule("users", "del_flag", "in", Arrays.asList(1, 2, 3, 4, 5, 0)),
                new TableRule("users", "username", "=", "yuwen")
        );
        List<RuleFilter> filters = new ArrayList<>(rules.size());

        rules.forEach(rule -> filters.add(FilterFactory.create(rule)));
        FilterContext.setFilters(filters);
    }

    @Test
    public void testSelect() {
        User user = new User();
        user.setUsername("yuwen");
        List<User> users = userDao.findAll();
        users = userDao.findByUser(user);
        users = userDao.findByUsernamePassword("yuwen", "pass");
        users = userDao.findByUserAndPassword(user, "pass");
        System.out.println(users);
    }

    @Test
    public void testUpdate() {
        int effectRow = userDao.update();
        System.out.println(effectRow);
    }
}
