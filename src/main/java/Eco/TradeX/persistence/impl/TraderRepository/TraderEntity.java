package Eco.TradeX.persistence.impl.TraderRepository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "trader")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(unique = true)
    @NotBlank
    private String username;
    @Column(unique = true)
    @NotBlank
    private String email;
    @Column
    @NotNull
    @Size(min = 12, message = "Password must be at least 12 characters long")
    private String password;
    @Column
    @NotNull
    private TraderStatus status;
}
