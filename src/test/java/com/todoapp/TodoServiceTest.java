package com.todoapp;

import com.todoapp.domain.notification.entity.Notification;
import com.todoapp.domain.notification.repository.NotificationRepository;
import com.todoapp.domain.notification.service.NotificationService;
import com.todoapp.domain.team.entity.Team;
import com.todoapp.domain.team.entity.TeamMember;
import com.todoapp.domain.team.repository.TeamMemberRepository;
import com.todoapp.domain.team.service.TeamService;
import com.todoapp.domain.todo.entity.Todo;
import com.todoapp.domain.todo.repository.TodoRepository;
import com.todoapp.domain.todo.service.TodoService;
import com.todoapp.domain.user.entity.User;
import com.todoapp.domain.user.repository.UserRepository;
import com.todoapp.global.security.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtProvider jwtProvider;

    // 테스트용 유저 생성
    private User createUser(String email, String name) {
        return userRepository.save(User.builder()
                .email(email)
                .password("test1234")
                .name(name)
                .role(User.Role.USER)
                .build());
    }

    // 테스트용 팀 생성
    private Team createTeam(User owner) {
        Long teamId = teamService.createTeam("테스트팀", "설명", owner);
        return teamService.findById(teamId);
    }

    @Test
    @DisplayName("JWT 토큰 생성 후 검증이 정상 통과한다")
    void jwtTokenTest() {
        // given
        String email = "jwt@test.com";

        // when
        String token = jwtProvider.createToken(email);

        // then
        assertThat(jwtProvider.validateToken(token)).isTrue();
        assertThat(jwtProvider.getEmail(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("담당자 지정 시 알림이 생성된다")
    void createTodoWithAssignedNotification() {
        // given
        User owner = createUser("owner@test.com", "팀장");
        User member = createUser("member@test.com", "팀원");
        Team team = createTeam(owner);
        teamService.inviteMember(team, owner, member);

        // when
        todoService.createTodo(team, "테스트 투두", "내용",
                Todo.Priority.HIGH, null, member, owner);

        // then
        long count = notificationRepository.countByUserAndIsReadFalse(member);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("팀장이 아닌 사람이 투두 삭제 시도하면 예외가 발생한다")
    void deleteTodoByNonOwnerThrowsException() {
        // given
        User owner = createUser("owner2@test.com", "팀장");
        User member = createUser("member2@test.com", "팀원");
        Team team = createTeam(owner);
        teamService.inviteMember(team, owner, member);

        // when & then
        assertThatThrownBy(() -> teamService.validateTeamOwner(team, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("팀장만 가능한 작업입니다.");
    }
}