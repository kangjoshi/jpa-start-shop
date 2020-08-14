package edu.til.jpastartshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    public Member() {
    }

    public Member(long id, String name) {
        this(id, name, new Address());
    }

    public Member(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }


    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
