import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class RestfulBookerTests {

    @Test //Test1 funcionando
    public void getBookingByIdTest() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Usar un id de reserva existente, puedes obtener un id con el endpoint /booking
        int bookingId = 11;

        // Enviar la solicitud GET
        Response response = RestAssured.given()
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}");

        // Verificar que el status code sea 200
        response.then().assertThat().statusCode(200);

        // Verificar que el cuerpo de la respuesta tenga el formato correcto
        response.then().assertThat().body("firstname", equalTo("Jane"));
        response.then().assertThat().body("lastname", equalTo("Doe"));
        response.then().assertThat().body("totalprice", equalTo(111));
        response.then().assertThat().body("depositpaid", equalTo(true));
        response.then().assertThat().body("bookingdates.checkin", equalTo("2018-01-01"));
        response.then().assertThat().body("bookingdates.checkout", equalTo("2019-01-01"));
        response.then().assertThat().body("additionalneeds", equalTo("Extra pillows please"));

        // Mostrar en la consola el cuerpo de la respuesta
        response.then().log().body();
    }

    @Test //test 2 funcionando
    public void createBookingTest() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Booking booking = new Booking();
        booking.setFirstname("Kevinnn");
        booking.setLastname("Valdiviaa");
        booking.setTotalprice(999);
        booking.setDepositpaid(true);

        Booking.BookingDates bookingDates = new Booking.BookingDates();
        bookingDates.setCheckin("2018-01-01");
        bookingDates.setCheckout("2019-01-01");
        booking.setBookingdates(bookingDates);

        booking.setAdditionalneeds("PenHouse");

        ObjectMapper mapper = new ObjectMapper();
        String bookingPayload = mapper.writeValueAsString(booking);
        System.out.println("Request Payload: " + bookingPayload);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(bookingPayload)
                .when()
                .post("/booking");

        // Verificar que el status code sea 200
        response.then().assertThat().statusCode(200);

        // Verificar que el cuerpo de la respuesta tenga el formato correcto
        response.then().assertThat().body("booking.firstname", equalTo(booking.getFirstname()));
        response.then().assertThat().body("booking.lastname", equalTo(booking.getLastname()));
        response.then().assertThat().body("booking.totalprice", equalTo(booking.getTotalprice()));
        response.then().assertThat().body("booking.depositpaid", equalTo(booking.isDepositpaid()));
        response.then().assertThat().body("booking.bookingdates.checkin", equalTo(booking.getBookingdates().getCheckin()));
        response.then().assertThat().body("booking.bookingdates.checkout", equalTo(booking.getBookingdates().getCheckout()));
        response.then().assertThat().body("booking.additionalneeds", equalTo(booking.getAdditionalneeds()));

        // Mostrar en la consola el cuerpo de la respuesta
        response.then().log().body();
    }

    @Test //test3 funcionando
    public void testNonExistentBookingId() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        // Asumimos que el ID 999999999 no existe.
        int nonExistentId = 999999999;

        Response response = RestAssured
                .given()
                .pathParam("id", nonExistentId)
                .when()
                .get("/booking/{id}")
                .then()
                .statusCode(404)
                .contentType("text/plain")
                .body(equalTo("Not Found"))
                .extract()
                .response();

        // Imprimir detalles de la respuesta para depuración
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Test //test 4
    public void testCreateBookingWithEmptyData() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String bookingPayload = """
        {
            "firstname" : "",
            "lastname" : "",
            "totalprice" : 0,
            "depositpaid" : true,
            "bookingdates" : {
                "checkin" : "",
                "checkout" : ""
            },
            "additionalneeds" : ""
        }
        """;

        // Enviar la solicitud POST
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(bookingPayload)
                .when()
                .post("/booking");

        // Verificar que el status code sea 400 Bad request
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("message", Matchers.containsString("Bad Request"));
    }

    @Test //test 5
    public void GetBookingIdsTest()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com/booking";
        Response response = RestAssured.when().get();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("size()", not(0));
        response.then().log().body();

    }

}
