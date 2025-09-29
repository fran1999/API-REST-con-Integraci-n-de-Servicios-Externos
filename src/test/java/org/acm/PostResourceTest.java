package org.acm;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.acm.dto.MergedPostDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PostResourceTest {

    @Test
    void shouldMergePostsCommentsAndUsers() {
        List<MergedPostDto> list =
                given()
                        .when().get("/posts")
                        .then().statusCode(200)
                        .extract().as(new TypeRef<List<MergedPostDto>>() {});

        assertFalse(list.isEmpty());
        MergedPostDto first = list.get(0);
        assertNotNull(first.title);
        assertNotNull(first.author);
        assertNotNull(first.comments);
    }

}
