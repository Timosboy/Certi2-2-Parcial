import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsSauceDemo extends BaseTest {
        @Test // Test 1 Funcionando
        public void removeProductTest() {
                WebElement userNameTextbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
                userNameTextbox.sendKeys("standard_user");

                WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
                passwordTextBox.sendKeys("secret_sauce");

                WebElement loginButton = driver.findElement(By.name("login-button"));
                loginButton.click();

                // 3productos Agregados
                WebElement firstAddToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions
                                                .presenceOfElementLocated(By.name("add-to-cart-sauce-labs-backpack")));
                firstAddToCartButton.click();

                WebElement secondAddToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(
                                                By.name("add-to-cart-sauce-labs-bike-light")));
                secondAddToCartButton.click();

                WebElement thirdAddToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(
                                                By.name("add-to-cart-sauce-labs-bolt-t-shirt")));
                thirdAddToCartButton.click();

                // Click en el icono de carrito
                WebElement cartIcon = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("shopping_cart_container")));
                cartIcon.click();

                // Paso 4: Contar el número de productos en el carrito antes de eliminar
                List<WebElement> productsInCartBeforeRemoval = driver.findElements(By.className("cart_item"));
                // Paso 5: Hacer click en los botones "Remove"
                for (WebElement product : productsInCartBeforeRemoval) {
                        WebElement removeButton = product.findElement(By.cssSelector(".cart_button"));
                        removeButton.click();
                }

                // Paso 6: Verificar que los productos fueron eliminados correctamente
                List<WebElement> productsInCartAfterRemoval = driver.findElements(By.className("cart_item"));
                int finalProductCount = productsInCartAfterRemoval.size();

                // Verificación: El número de productos después de la eliminación debería ser 0
                assertEquals(0, finalProductCount,
                                "El carrito debería estar vacío después de eliminar todos los productos.");

        }

        @Test // Test 2 Funcionando
        public void testAddToCartButtons() {

                WebElement userNametextbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
                userNametextbox.sendKeys("standard_user");

                WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
                passwordTextBox.sendKeys("secret_sauce");

                WebElement LoginButton = driver.findElement(By.name("login-button"));
                LoginButton.click();

                List<WebElement> addToCartButtons = driver
                                .findElements(By.xpath("//*[@id=\"add-to-cart-sauce-labs-fleece-jacket\"]"));

                for (WebElement addToCartButton : addToCartButtons) {
                        addToCartButton.click();

                        // Verificar que el texto del botón cambie a "Remove"
                        WebElement removeButton = driver
                                        .findElement(By.xpath("//*[@id=\"remove-sauce-labs-fleece-jacket\"]"));
                        assert (removeButton != null && removeButton.isDisplayed());
                }

        }

        @Test // Test 3 Funcionando
        public void testCartQuantityIncrements() {
                WebElement userNametextbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
                userNametextbox.sendKeys("standard_user");

                WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
                passwordTextBox.sendKeys("secret_sauce");

                WebElement LoginButton = driver.findElement(By.name("login-button"));
                LoginButton.click();

                // "Add to cart"
                List<WebElement> addToCartButtons = driver.findElements(By.id("add-to-cart-sauce-labs-fleece-jacket"));

                int expectedCartCount = 0;

                for (WebElement addToCartButton : addToCartButtons) {
                        addToCartButton.click();

                        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-fleece-jacket"));
                        assert (removeButton != null && removeButton.isDisplayed());

                        expectedCartCount++;

                        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
                        int actualCartCount = Integer.parseInt(cartBadge.getText());
                        assert (actualCartCount == expectedCartCount);
                }
        }

        @Test // Test 4 funcionando
        public void testCheckoutEmptyCart() {
                WebElement userNametextbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
                userNametextbox.sendKeys("standard_user");

                WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
                passwordTextBox.sendKeys("secret_sauce");

                WebElement LoginButton = driver.findElement(By.name("login-button"));
                LoginButton.click();

                // clic en el icono del carrito de compra
                WebElement cartIcon = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
                cartIcon.click();

                // Verificar que el carrito esté vacío
                WebElement checkoutButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
                checkoutButton.click();

                // Verificar que se muestra el mensaje de error
                WebElement errorMensaje = driver.findElement(By.className("error-message-container error"));
                String errorText = errorMensaje.getText();
                // Epic sadface: Username and password do not match any user in this service
                Assertions.assertTrue(errorMensaje.isDisplayed());
                Assertions.assertEquals("No existen productos en el carrito", errorText);
        }

        @Test // Test 5 Funcionando
        public void testCheckoutWithEmptyFields() {
                driver.get("https://www.saucedemo.com");

                // Login
                WebElement userNameTextbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
                userNameTextbox.sendKeys("standard_user");

                WebElement passwordTextBox = driver.findElement(By.id("password"));
                passwordTextBox.sendKeys("secret_sauce");

                WebElement loginButton = driver.findElement(By.name("login-button"));
                loginButton.click();

                // Click en el carrito
                WebElement cartIcon = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));
                cartIcon.click();

                // Click en Checkout
                WebElement checkoutButton = driver.findElement(By.id("checkout"));
                checkoutButton.click();

                // Caso 1: Campos vacíos
                WebElement firstNameField = driver.findElement(By.id("first-name"));
                WebElement lastNameField = driver.findElement(By.id("last-name"));
                WebElement postalCodeField = driver.findElement(By.id("postal-code"));
                WebElement continueButton = driver.findElement(By.id("continue"));

                // Dejar 3,2,1 campos vacios
                firstNameField.clear();
                lastNameField.clear();
                postalCodeField.clear();
                continueButton.click();
                // Otra forma de cssSelector Formato:
                // input[placeholder='Password']
                WebElement errorMensaje = driver.findElement(By.cssSelector(
                                "#checkout_info_container > div > form > div.checkout_info > div.error-message-container.error"));
                String errorText = errorMensaje.getText();
                // Error: First Name is required
                // Error: Last Name is required
                // Error: Postal Code is required
                Assertions.assertTrue(errorMensaje.isDisplayed());
                Assertions.assertEquals("Error: First Name is required", errorText);

        }

        @Test
        public void searchBylinkText() {

                // Login
                WebElement userNameTextbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
                userNameTextbox.sendKeys("standard_user");

                WebElement passwordTextBox = driver.findElement(By.id("password"));
                passwordTextBox.sendKeys("secret_sauce");

                WebElement loginButton = driver.findElement(By.name("login-button"));
                loginButton.click();

                WebElement twitterLink = driver.findElement(By.linkText("Twitter"));
                twitterLink.click();

                // Cambiar al contexto de la nueva pestaña que se abre con el enlace de Twitter
                ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1)); // Cambia a la segunda pestaña

                // Verificar que la URL sea correcta
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.urlContains("x.com/saucelabs"));

                driver.close();
                driver.switchTo().window(tabs.get(0));

        }
}
