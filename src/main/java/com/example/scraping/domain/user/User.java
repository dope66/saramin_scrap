package com.example.scraping.domain.user;

        import com.example.scraping.domain.scrap.ScrapEntity;
        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ScrapEntity> scraps;
}
