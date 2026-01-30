    package com.example.online.event.entity;

    import com.example.online.common.entity.BaseEntity;

    import jakarta.persistence.Entity;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;

    @Entity
    @Table(name = "decoration_images")
    public class DecorationImage extends BaseEntity {

        @ManyToOne(fetch = FetchType.LAZY)
        private Decoration decoration;

        private String imageUrl;

        // getters & setters
    }
