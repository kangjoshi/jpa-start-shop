package edu.til.jpastartshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

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
        this.id = id;
        this.name = name;
    }

    /*
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    */
}
