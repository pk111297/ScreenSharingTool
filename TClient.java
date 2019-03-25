import java.net.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
class GetIP extends JFrame
{
private JLabel label1,label2,label3;
private JTextField t1,t2,t3;
private JButton b1,b2;
private Container c;
public String ip;
public int port,randomNumber; 
private Frameeg frameeg;
GetIP(Frameeg frameeg)
{
this.frameeg=frameeg;
try
{ 
c=getContentPane(); // get the address of the container on which we will be placing our compopnets
c.setLayout(null);
label1=new JLabel("IP Address :");
label2=new JLabel("Port :");
label3=new JLabel("Unique ID :");
t1=new JTextField(" ");
t2=new JTextField(" ");
t3=new JTextField(" ");
b1=new JButton("Connect");
b2=new JButton("Cancel");
Font f=new Font("Verdana",Font.PLAIN,20);
label1.setFont(f);
label2.setFont(f);
label3.setFont(f);
t1.setFont(f);
t2.setFont(f);
t3.setFont(f);
b1.setFont(f);
b2.setFont(f);
label1.setBounds(30,40,150,25);
label2.setBounds(30,40+30+15,150,25);
label3.setBounds(30,40+30+30+15+15,150,25);
t1.setBounds(30+150+5,40,200,25);
t2.setBounds(30+150+5,40+30+15,200,25);
t3.setBounds(30+150+5,40+30+30+15+15,200,25);
b1.setBounds(30,40+30+30+30+15+15+15,200,25);
b2.setBounds(30+150+5,40+30+30+30+15+15+15,200,25);
c.add(label1);
c.add(label2);
c.add(label3);
c.add(t1);
c.add(t2);
c.add(t3);
c.add(b1);
c.add(b2);
b1.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae){
ip=t1.getText().trim();      
port=Integer.parseInt(t2.getText().trim());      
randomNumber=Integer.parseInt(t3.getText().trim());      
System.out.println(ip+port+randomNumber);
TClient tc=new TClient(ip,port,randomNumber,GetIP.this);
frameeg.addTClient(tc);
}
});
b2.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae){
System.exit(0);
}
});
setDefaultCloseOperation(EXIT_ON_CLOSE);
setSize(415,280);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation(d.width/2-207,d.height/2-140);
setVisible(true);
}catch(Exception e)
{
System.out.println(e);
}
}
public String IP()
{
return this.ip;
}
public int port()
{
return this.port;
}
public int randomNumber()
{
return this.randomNumber;
}
}
class Frameeg extends Frame implements WindowListener 
{ 
private GetIP getIP;
Frameeg()  
{
addWindowListener(this);
getIP=new GetIP(this);
//tc=new TClient();
}
public void addTClient(TClient tc)
{
setLayout(new BorderLayout());  
add(tc,BorderLayout.CENTER);  
setLocation(0,0);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setSize(d.width,d.height);  
setVisible(true);  
}
public void windowOpened(WindowEvent w)
{
}
public void windowClosing(WindowEvent w)
{
//System.out.println("Hello hii");
this.dispose();
System.exit(0);
}
public void windowClosed(WindowEvent w)
{
}
public void windowIconified(WindowEvent w)
{
}
public void windowDeiconified(WindowEvent w)
{
}
public void windowActivated(WindowEvent w)
{
}
public void windowDeactivated(WindowEvent w)
{
}
public static void main(String g[])
{
Frameeg f=new Frameeg();
}
}
class TClient extends Canvas implements MouseMotionListener,MouseListener,KeyListener
{
//public static String ip="192.168.137.1";
//public static String ip="169.254.47.236";
//public static int port=1202;
public static String ip="";
public int randomNumber;
public int port;
public BufferedImage bi;
public TClient(String ip,int port,int randomNumber,GetIP getIP) 
{
this.ip=ip;
this.port=port;
this.randomNumber=randomNumber;
System.out.println(ip+port+randomNumber);
try
{
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
String request="UID_"+randomNumber+"#";
System.out.println(request);
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(os);
outputStreamWriter.write(request);
outputStreamWriter.flush();
byte ack[]=new byte[1];
is.read(ack);
System.out.println(ack[0]);
if(ack[0]==125)
{
socket.close();
System.exit(0);
}
}catch(Exception e)
{
System.out.println(e);
getIP.dispose();
System.exit(0);
}
getIP.dispose();
addMouseMotionListener(this);
addMouseListener(this);
addKeyListener(this);
this.ImageTask();
}
public void ImageTask()
{
java.util.Timer timer=new java.util.Timer();
timer.scheduleAtFixedRate(new TimerTask(){
public void run()
{
try
{
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
String request="IM#";
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(os);
outputStreamWriter.write(request);
outputStreamWriter.flush();
int x=0;
byte[] length=new byte[4];
is.read(length);
int byteArrayLength=(length[3] & 0xFF | (length[2] & 0xFF)<<8 | (length[1] & 0xFF)<<16 | (length[0] & 0xFF)<<24);
int acknowlegment=85;
byte[] ack=new byte[4];
ack[0]=(byte)(acknowlegment>>24);
ack[1]=(byte)(acknowlegment>>16);
ack[2]=(byte)(acknowlegment>>8);
ack[3]=(byte)(acknowlegment>>0);
os.write(ack);
os.flush();
//FileOutputStream fos=new FileOutputStream("captureScreen.png");
ByteArrayOutputStream baos=new ByteArrayOutputStream();
x=0;
byte[] imageArray=new byte[1024];
int a=0;
while(x<byteArrayLength)
{
if(byteArrayLength-x>=1024) 
{
a=is.read(imageArray,0,1024);
baos.write(imageArray,0,1024);
//fos.write(imageArray,0,1024);
}
else 
{
a=is.read(imageArray,0,byteArrayLength-x);
baos.write(imageArray,0,byteArrayLength-x);
//fos.write(imageArray,0,1024);
}
//System.out.println("loop");
x=x+a;
os.write(ack);
os.flush();
}
is.read(new byte[is.available()]);
os.write(ack);
os.flush();
ByteArrayInputStream bis=new ByteArrayInputStream(baos.toByteArray());
TClient.this.bi=ImageIO.read(bis);
TClient.this.repaint();
socket.close();
}catch(IOException ioe)
{
System.out.println(ioe);
}
catch(Exception e)
{
System.out.println(e);
}
}
},
0*1000,3*1000);
}
public void paint(Graphics g)  
{
try
{
//System.out.println("Paint Chali");
g.drawImage(this.bi,0,0,null);
}catch(Exception ioe)
{
System.out.println(ioe);
}
}
public void update(Graphics g)
{
try
{
//System.out.println("Update Chali");
g.drawImage(this.bi,0,0,null);
}catch(Exception ioe)
{
System.out.println(ioe);
}
}  
public void mouseDragged(MouseEvent e)
{
try
{
//System.out.println("MouseDragged");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
//System.out.println(e.getX()+"   "+e.getY());
String gh="MD_"+e.getX()+"_"+e.getY()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println(ex);
}
}
public void mouseMoved(MouseEvent e)
{
try
{
//System.out.println("MouseMoved");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
//System.out.println(e.getX()+"   "+e.getY());
String gh="MM_"+e.getX()+"_"+e.getY()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println(ex);
}
}
public void mouseExited(MouseEvent e)
{
}
public void mousePressed(MouseEvent e)
{
try
{
//System.out.println("MousePressed");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
//System.out.println(e.getX()+"   "+e.getY());
String gh="MP_"+e.getX()+"_"+e.getY()+"_"+e.getButton()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println(ex);
}
}
public void mouseReleased(MouseEvent e)
{
try
{
//System.out.println("MouseReleased");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
//System.out.println(e.getX()+"   "+e.getY());
String gh="MR_"+e.getX()+"_"+e.getY()+"_"+e.getButton()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println(ex);
}
}
public void mouseEntered(MouseEvent e)
{
}
public void mouseClicked(MouseEvent e)
{
try
{
//System.out.println("MouseClicked");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
//System.out.println(e.BUTTON1+"  "+e.BUTTON2+"  "+e.BUTTON3);
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
//System.out.println(e.getX()+"   "+e.getY());
String gh="MC_"+e.getX()+"_"+e.getY()+"_"+e.getButton()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println(ex);
}
}
public void keyPressed(KeyEvent k)
{
try
{
//System.out.println("Keypressed");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
String gh="KP_"+k.getKeyCode()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println("keyPressed Exception");
System.out.println(ex);
}
}
public void keyReleased(KeyEvent k)
{
try
{
//System.out.println("KeyReleased");
Socket socket=new Socket(ip,port);
InputStream is=socket.getInputStream();
OutputStream osw=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(osw);
String gh="KR_"+k.getKeyCode()+"#";
//System.out.println(gh);
outputStreamWriter.write(gh);
outputStreamWriter.flush();
byte b[]=new byte[1];
is.read(b);
socket.close();
}catch(Exception ex)
{
System.out.println("keyReleased Exception");
System.out.println(ex);
}
}
public void keyTyped(KeyEvent k)
{
}
}
