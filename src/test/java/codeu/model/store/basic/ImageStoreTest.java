package codeu.model.store.basic;

import codeu.model.data.Image;
import codeu.model.data.UserAction;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class ImageStoreTest {

    private ImageStore imageStore;
    private PersistentStorageAgent mockPersistentStorageAgent;

    private final Image one =
            new Image("one", "one", UUID.randomUUID(), Instant.now());
    private final Image two =
            new Image("two", "two", UUID.randomUUID(), Instant.now());
    private final Image three =
            new Image("three", "three", UUID.randomUUID(), Instant.now());

    @Before
    public void setup() {
        mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
        imageStore = ImageStore.getTestInstance(mockPersistentStorageAgent);

        final List<Image> imageList = new ArrayList<>();
        imageList.add(0, one);
        imageList.add(0, two);
        imageList.add(0, three);
        imageStore.setImages(imageList);
    }

    @Test
    public void testReturnImages() {
        List<Image> resultImages = imageStore.returnAllImages();

        Assert.assertEquals(3, resultImages.size());
        assertEquals(one, resultImages.get(2));
        assertEquals(two, resultImages.get(1));
        assertEquals(three, resultImages.get(0));
    }

    @Test
    public void testAddUserAction() {
        Image image = new Image("test", "test", UUID.randomUUID(), Instant.now());

        imageStore.addImage(image);
        Image resultImage = imageStore.returnAllImages().get(0);

        assertEquals(image, resultImage);
        Mockito.verify(mockPersistentStorageAgent).writeThrough(image);
    }

    private void assertEquals(Image expectedImage, Image actualImage) {
        Assert.assertEquals(expectedImage.getId(), actualImage.getId());
        Assert.assertEquals(expectedImage.returnDestination(), actualImage.returnDestination());
        Assert.assertEquals(expectedImage.returnFilename(), actualImage.returnFilename());
    }
}
