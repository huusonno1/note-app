package internsafegate.noteapp.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthDTO {
    @JsonProperty("username")
    private String username = "";

    @JsonProperty("password")
    @NotBlank(message = "Password cannot be black")
    private String password = "";

    @JsonProperty("retype_password")
    private String retypePassword = "";

    @JsonProperty("email")
    private String email = "";

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

}
