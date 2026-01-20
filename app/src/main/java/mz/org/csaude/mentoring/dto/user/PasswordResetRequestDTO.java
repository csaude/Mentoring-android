package mz.org.csaude.mentoring.dto.user;

public class PasswordResetRequestDTO {

    // IMPORTANT:
    // If backend expects "email", this works.
    // If backend expects "username", add it too. Unknown fields are usually ignored server-side.
    private String email;
    private String username;

    public PasswordResetRequestDTO() {}

    public PasswordResetRequestDTO(String emailOrUsername) {
        this.email = emailOrUsername;
        this.username = emailOrUsername;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
