package com.example.house.batch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity
public class Lawd extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lawdId;

    @Column(unique = true, length = 10)
    private String lawdCode;
    @Column(length = 100)
    private String lawdProvince;

    private Boolean exist;

    public Lawd(String lawdCode, String lawdProvince, Boolean exist) {
        this.lawdCode = lawdCode;
        this.lawdProvince = lawdProvince;
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "Lawd{" + "lawdId=" + lawdId
            + ", lawdCode='" + lawdCode + '\''
            + ", lawdProvince='" + lawdProvince + '\''
            + ", exist=" + exist
            + '}';
    }

    public void update(String lawdCode, String lawdProvince, Boolean exist) {
        this.lawdCode = lawdCode;
        this.lawdProvince = lawdProvince;
        this.exist = exist;
    }
}
