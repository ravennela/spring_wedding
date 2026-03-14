package com.example.online.vendor.entity;

import com.example.online.common.entity.BaseEntity;
import com.example.online.event.entity.Decoration;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "vendor_decorations", uniqueConstraints = @UniqueConstraint(columnNames = { "vendor_id",
    "decoration_id" }))
public class VendorDecoration extends BaseEntity {

  @ManyToOne
  private Vendor vendor;

  public void setVendor(Vendor vendor) {
    this.vendor = vendor;
  }

  public void setDecoration(Decoration decoration) {
    this.decoration = decoration;
  }

  public void setAvailable(boolean isAvailable) {
    this.isAvailable = isAvailable;
  }

  @ManyToOne
  private Decoration decoration;

  private boolean isAvailable = true;

  public Vendor getVendor() {
    return vendor;
  }

  public Decoration getDecoration() {
    return decoration;
  }

  public boolean isIsAvailable() {
    return isAvailable;
  }
}



