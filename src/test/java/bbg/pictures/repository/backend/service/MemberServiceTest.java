package bbg.pictures.repository.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


import java.util.List;
import java.util.Optional;

import bbg.pictures.repository.backend.model.Member;
import bbg.pictures.repository.backend.repository.MemberRepository;
import bbg.pictures.repository.backend.validation.MemberValidator;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = MemberService.class)
class MemberServiceTest {
    @MockBean
    private MemberRepository memberRepositoryMock;
    @MockBean
    private MemberValidator validatorMock;

    @Autowired
    private MemberService objectUnderTest;

    @Test
    void shouldSave_whenNonConflictingMembersAreProvided() {
        doReturn(false).when(memberRepositoryMock).existsById(any());


        objectUnderTest.save(new Member());

        verify(validatorMock).validateOnSave(any());
        verify(memberRepositoryMock).existsById(any());
        verify(memberRepositoryMock).save(any());
    }

    @Test
    void shouldThrowException_onSave_whenConflictingMembersAreProvided() {
        doReturn(true).when(memberRepositoryMock).existsById(any());


        assertThatThrownBy(() -> objectUnderTest.save(new Member()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Member with name 'null' already exists");

        verify(validatorMock).validateOnSave(any());
        verify(memberRepositoryMock).existsById(any());
        verify(memberRepositoryMock, never()).save(any());
    }

    @Test
    void shouldReturnAllMembers_onFindAll() {
        final List<Member> expected = List.of(new Member(), new Member());
        doReturn(expected).when(memberRepositoryMock).findAll();

        final List<Member> actual = (List<Member>) objectUnderTest.findAll();
        assertThat(actual).isEqualTo(expected);

        verify(memberRepositoryMock).findAll();
    }

    @Test
    void shouldReturnCorrectMember_onFindByName_whenMemberExists() {
        final Member expected = new Member();
        doReturn(Optional.of(expected)).when(memberRepositoryMock).findById("name");

        final Member actual = objectUnderTest.findByName("name");
        assertThat(actual).isEqualTo(expected);

        verify(memberRepositoryMock).findById("name");
    }

    @Test
    void shouldThrowException_onFindByName_whenMemberDoesNotExist() {
        doReturn(Optional.empty()).when(memberRepositoryMock).findById("name");

        assertThatThrownBy(() -> objectUnderTest.findByName("name"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Member with name 'name' does not exist");

        verify(memberRepositoryMock).findById("name");
    }

    @Test
    void shouldUpdate_whenMemberExists() {
        final Member expected = new Member();
        doReturn(Optional.of(expected)).when(memberRepositoryMock).findById("name");

        objectUnderTest.update("name", new Member());

        verify(memberRepositoryMock).findById("name");
        verify(memberRepositoryMock).save(expected);
    }

    @Test
    void shouldThrowException_onUpdate_whenMemberDoesNotExist() {
        doReturn(Optional.empty()).when(memberRepositoryMock).findById("name");

        assertThatThrownBy(() -> objectUnderTest.update("name", new Member()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Member with name 'name' does not exist");

        verify(memberRepositoryMock).findById("name");
        verify(memberRepositoryMock, never()).save(any());
    }

    @Test
    void shouldDelete() {
        objectUnderTest.delete("name");

        verify(memberRepositoryMock).deleteById("name");
    }
}