package it.mirea.kursovayabread;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {
    //регистрация в единичном случае
    public Username userRegistration(String mail, String password) {
        if (mail.equals("user10@mail.ru") && password.equals("qwerty")) {
            return new Username("user10", mail, password, "89031241370");
        }
        return null;
    }

    //тестирование на корректность данных регистрации
    @Test
    public void correctUserInfo() {
        Username user = userRegistration("user10@mail.ru", "qwerty");
        assertEquals("user10@mail.ru", user.getEmail());
        assertEquals("qwerty", user.getPass());
        assertTrue(user.isUser());
        assertNull(userRegistration("user@mail.ru", "qwerty"));
        assertNull(userRegistration("user10@mail.ru", "123456"));
    }
    //аля механника изменения роли работает корректно
    @Test
    public void correctRoleInfo() {
        Username user = userRegistration("user10@mail.ru", "qwerty");
        assertTrue(user.isUser());
        assertFalse(user.isAdmin());
        user.setAdmin();
        assertFalse(user.isUser());
        assertTrue(user.isAdmin());
    }

    //захардкодил аля бд по продукции
    int k = 0;
    int[] IDs = new int[10];
    public Product productGeneration(int id, String name, int price) {
        for (int i : IDs) {
            if (id == i) return null;
        }
        IDs[k] = id;
        k+=1;
        return new Product(id, name, price);
    }

    //проверяю аля механнику, что два раза одинаковое id использовать нельзя
    @Test
    public void correctProducts() {
        Product product1 = productGeneration(1, "milk", 100);
        Product product2 = productGeneration(2, "sugar", 50);
        Product product3 = productGeneration(1, "butter", 80);
        assertEquals(1, product1.getId());
        assertEquals(50, product2.getPrice());
        assertNull(product3);
    }
}