package estudiante.com.finalproject

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    // Regla para abrir la MainActivity
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // ESTO ES LO NUEVO: Se ejecuta ANTES de la prueba
    @Before
    fun setUp()
    {
        // Obtenemos el contexto de la app y borramos las preferencias
        // Esto asegura que el robot siempre empiece con la sesión cerrada.
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
    }

    //TEST 1 func

    @Test
    fun testNavegacionYLoginFlujoCompleto()
    {
        // 1. Ir a la pestaña de Perfil
        onView(withId(R.id.nav_profile)).perform(click())

        // 2. Comprobar que estamos en la pantalla de Login viendo que el botón existe
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))

        // 3. Escribir correo y CERRAR EL TECLADO
        onView(withId(R.id.etLoginEmail))
            .perform(typeText("carlos.m@email.com"), closeSoftKeyboard())

        // 4. Hacer clic en el botón de Iniciar Sesión
        onView(withId(R.id.btnLogin)).perform(click())

        // 5. Afirmar (Assert) que la interfaz cambió y ahora vemos el perfil
        onView(withId(R.id.layoutProfile)).check(matches(isDisplayed()))

        // 6. Afirmar que el botón de Cerrar Sesión está visible
        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()))
    }
    // TEST 2, navegar a profile
    @Test
    fun testNavegacionProfile()
    {
        //VA A LA PESTAÑA DE PERFIL
        onView(withId(R.id.nav_profile)).perform(click())

        //BOTON LOGIN ES CORRECTO (ESTA EN PANTALLA CORRECTA)
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    //TEST 3 TEST NAVEGACION es visible ak abrir la app
    @Test
    fun testBottomNavVisible()
    {
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
    }

    //TEST 4 contenedor donde se muestran todos los fragments
    @Test
    fun testFragContainer() {
        onView(withId(R.id.fragmentContainer)).check(matches(isDisplayed()))
    }
    //test 5 nav valida en search view
    @Test
    fun testNavegacionBusqueda()
    {
        onView(withId(R.id.nav_search)).perform(click())
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
    }

}