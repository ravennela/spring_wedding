
package com.example.online.vender.entity;

import com.example.online.common.entity.BaseEntity;
import com.example.online.location.enitity.City;
import com.example.online.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendors")
public class Vendor extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;

    @ManyToOne
    private City city;

    private boolean isActive = true;
}
