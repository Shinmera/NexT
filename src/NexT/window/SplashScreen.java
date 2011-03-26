package NexT.window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class SplashScreen extends Frame implements MouseListener  {
  public boolean stretch = false;
  public SplashScreen(String aImageId) {
    if ( aImageId == null || aImageId.trim().length() == 0 ){
      throw new IllegalArgumentException("Image Id does not have content.");
    }
    fImageId = aImageId;
  }

  /**
  * Show the splash screen to the end user.
  *
  * <P>Once this method returns, the splash screen is realized, which means
  * that almost all work on the splash screen should proceed through the event
  * dispatch thread. In particular, any call to <tt>dispose</tt> for the
  * splash screen must be performed in the event dispatch thread.
  */
  public void splash(){
    initImageAndTracker();
    if(fImage!=null){
        if(!stretch)setSize(fImage.getWidth(NO_OBSERVER), fImage.getHeight(NO_OBSERVER));
        else setSize(Toolkit.getDefaultToolkit().getScreenSize());
        if(stretch)fImage = fImage.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
                                                     Toolkit.getDefaultToolkit().getScreenSize().height,
                                                     Image.SCALE_FAST);
        center();

        fMediaTracker.addImage(fImage, IMAGE_ID);
        try {fMediaTracker.waitForID(IMAGE_ID);}
        catch(InterruptedException ex){System.out.println("Cannot track image load.");}

        SplashWindow splashWindow = new SplashWindow(this,fImage);
      }
  }

  // PRIVATE//
  private final String fImageId;
  private MediaTracker fMediaTracker;
  private Image fImage;
  private static final ImageObserver NO_OBSERVER = null;
  private static final int IMAGE_ID = 0;

  private void initImageAndTracker(){
    fMediaTracker = new MediaTracker(this);
    File imageURL = new File(fImageId);
    try {fImage = ImageIO.read(imageURL);
    } catch (IOException ex) { ex.printStackTrace();}
  }

  private void center(){
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle frame = getBounds();
    setLocation((screen.width - frame.width)/2, (screen.height - frame.height)/2);
  }

  private class SplashWindow extends Window{
    SplashWindow(Frame aParent, Image aImage) {
       super(aParent);
       fImage = aImage;
       Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
       if(stretch) setSize(screen);
       else setSize(fImage.getWidth(NO_OBSERVER), fImage.getHeight(NO_OBSERVER));
       Rectangle window = getBounds();
       setLocation((screen.width - window.width) / 2,(screen.height - window.height)/2);
       setVisible(true);
    }
    public void paint(Graphics graphics) {
      if (fImage != null) {
        graphics.drawImage(fImage,0,0,this);
      }
    }
    private Image fImage;

        
  }
        public void mouseClicked(MouseEvent me) {
        }
        public void mousePressed(MouseEvent me) {
        }
        public void mouseReleased(MouseEvent me) {
            dispose();
        }
        public void mouseEntered(MouseEvent me) {
        }
        public void mouseExited(MouseEvent me) {
        }
}
