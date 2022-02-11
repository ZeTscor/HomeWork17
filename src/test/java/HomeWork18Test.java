
import filter.CustomLogFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;


import static filter.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

public class HomeWork18Test {
    String cookie;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @BeforeEach
    void login() {
        cookie = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", "grof@gmail.com")
                .formParam("Password", "azsxdc123")
                .when()
                .post("/login")
                .then()
                .extract().cookie("NOPCOMMERCE.AUTH");
    }

    @DisplayName("Проверка наличия картинки в корзине")
    @Test
    void addToCartTest() {
        String data = "addtocart_31.EnteredQuantity=1";
        Response response = given()
                .filter(customLogFilter().withCustomTemplates())
                .cookie("NOPCOMMERCE.AUTH", cookie)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body(data)
                .when()
                .post("/addproducttocart/details/31/1")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .extract().response();
        System.out.println(response.asString());
        assertThat(response.path("updateflyoutcartsectionhtml").toString().contains("img alt=\"Picture of 14.1-inch Laptop\""));
    }

    @Test
    @DisplayName("Добавления адрес для пользователя")
    void addAddressesTest() {
        //String data = "Address.Id=0&Address.FirstName=sfsd&Address.LastName=dsfsdf&Address.Email=eefd%40email.com&Address.Company=&Address.CountryId=78&Address.StateProvinceId=0&Address.City=Kiev&Address.Address1=Kresitic+1+2+3&Address.Address2=&Address.ZipPostalCode=1232145&Address.PhoneNumber=783242344234&Address.FaxNumber=";
        given()
                .filter(customLogFilter().withCustomTemplates())
                .cookie("NOPCOMMERCE.AUTH", cookie)
                .contentType("application/x-www-form-urlencoded")
                .formParam("Address.Id", 0)
                .formParam("Address.FirstName", "ASad")
                .formParam("Address.LastName", "DDSod")
                .formParam("Address.Email", "fdsfsdf@test.com")
                .formParam("Address.CountryId", 66)
                .formParam("Address.StateProvinceId", 0)
                .formParam("Address.City", "Moscow")
                .formParam("Address.Address1", "Novigradskai 23")
                .formParam("Address.ZipPostalCode", "114503")
                .formParam("Address.PhoneNumber", "795519864567")
                .when()
                .post("/customer/addressadd")
                .then()
                .statusCode(302);
    }

    @Test
    @DisplayName("Наличие кнопки удалеения адресса")
    void deleteAddressesButtonTest() {
        Response response = given()
                .filter(customLogFilter().withCustomTemplates())
                .cookie("NOPCOMMERCE.AUTH", cookie)
                .when()
                .get("/customer/addresses")
                .then()
                .extract().response();
        Assertions.assertTrue(response.asString().contains("button-2 edit-address-button"));


    }
}
