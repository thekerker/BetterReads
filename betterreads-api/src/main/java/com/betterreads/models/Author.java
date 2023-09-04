package com.betterreads.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * A document that represents an Author
 * </p>
 */
@Document(collection = "authors")
@Data
@AllArgsConstructor
@Builder
public class Author {

    @Id
    public String id;

    @NotEmpty(message = "Name is required")
    public Name name;

    private Date dateOfBirth;

    private String gender;

    private String city;

    private String state;

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dob = this.dateOfBirth != null ? df.format(this.dateOfBirth) : StringUtils.EMPTY;

        return "Author: [Id: " + this.getId() + ", Name: " + this.name.toString() +
                ", Date of Birth: " + dob + ", Gender: " + this.getGender() +
                ", Location: " + this.getCity() + ", " + this.getState() + "]";
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class Name {
        private String firstName;

        private String middleName;

        @NotBlank(message = "Last Name is required")
        private String lastName;

        private String suffix;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(this.lastName);

            if (StringUtils.isNotBlank(this.suffix)) {
                sb.append(" ").append(this.suffix);
            }

            if (StringUtils.isNotBlank(this.firstName)) {
                sb.append(", ").append(this.firstName);

                if (StringUtils.isNotBlank(this.middleName)) {
                    sb.append(" ").append(this.middleName);
                }
            }

            return sb.toString();
        }
    }
}
