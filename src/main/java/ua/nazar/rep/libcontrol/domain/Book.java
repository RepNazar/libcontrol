package ua.nazar.rep.libcontrol.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
//TODO maybe change toString
@ToString(of = {"id", "code", "name", "genre", "inStock", "owner"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Code cannot be empty")
    private String code;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    private String genre;
    private boolean inStock;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;
}
