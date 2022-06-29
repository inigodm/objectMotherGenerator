package inigo
import inigo.objectMotherCreator.infraestructure.IdeaShits
import inigo.objectMotherCreator.model.infoExtractor.OMParameter
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach

class `Needed info should be extracted in holders` {
    @MockK lateinit var param : OMParameter
    @MockK lateinit var project : IdeaShits

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        every { param.getClassCanonicalName() } returns "canonical"
    }
}
