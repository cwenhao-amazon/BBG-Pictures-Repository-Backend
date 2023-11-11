package bbg.pictures.repository.backend.test_utils;

import bbg.pictures.repository.backend.model.ImageData;
import bbg.pictures.repository.backend.model.dto.UpdatePasswordDto;
import bbg.pictures.repository.backend.model.dto.UserDto;

public class BuilderUtils {
    public static ImageData imageData(final Long id,
                                final String path,
                                final String uploadTimestamp,
                                final String uploaderName,
                                final String album) {
        final ImageData imageData = new ImageData();
        imageData.setId(id);
        imageData.setPath(path);
        imageData.setUploadTimestamp(uploadTimestamp);
        imageData.setUploaderName(uploaderName);
        imageData.setAlbum(album);

        return imageData;
    }

    public static UserDto user(final String username, final String password) {
        final UserDto user = new UserDto();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    public static UpdatePasswordDto updatePasswordDto(final String oldPassword, final String newPassword) {
        final UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setOldPassword(oldPassword);
        updatePasswordDto.setNewPassword(newPassword);

        return updatePasswordDto;
    }
}
