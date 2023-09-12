package com.betterreads.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private String id;

    private String firstName;

    private String middleName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    private String suffix;

    private Date dateOfBirth;

    private String gender;

    private String city;

    private String state;

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dob = this.dateOfBirth != null ? df.format(this.dateOfBirth) : StringUtils.EMPTY;
        String name = getFormattedName();

        return "Author: [Id: " + this.getId() + ", Name: " + name +
                ", Date of Birth: " + dob + ", Gender: " + this.getGender() +
                ", Location: " + this.getCity() + ", " + this.getState() + "]";
    }

    public String getFormattedName() {
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
