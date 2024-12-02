package com.project.springjavafx.springApp;

import com.project.springjavafx.javaFXApp.controller.TimeoffController;
import com.project.springjavafx.javaFXApp.data.dao.LeaveRequestDAO;
import com.project.springjavafx.javaFXApp.data.models.LeaveRequest;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(true)
class SpringJavaFxApplicationTests {

    static LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    static List<LeaveRequest> leaveRequest = leaveRequestDAO.getLeaveRequestsbyEmployeeId(1);

    @BeforeAll
    @Rollback
    static void before(){
        System.out.println("before");
        System.out.println("List size: " + leaveRequest.size());

    }

    @AfterAll
    @Rollback
    static void after(){
        System.out.println("After");
        LeaveRequest request = new LeaveRequest(leaveRequest.size() + 1, 1,  "Sick Leave", new Date(124,5,1), new Date(124,5,5), "Pending");
        boolean success = leaveRequestDAO.addLeaveRequest(request);

        List<LeaveRequest> updatedList = leaveRequestDAO.getLeaveRequestsbyEmployeeId(1);

        assert success;
        assert leaveRequest.size() < updatedList.size();

        System.out.println("Updated list size: " + updatedList.size());

        System.out.println("All godd");
    }

    @Test
    void contextLoads() {
        System.out.println("Alma");
    }

}
