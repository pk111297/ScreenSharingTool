import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.*;
import java.awt.event.InputEvent;
import javax.imageio.*;
import java.util.*;
import java.util.Random;
import javax.swing.*;
class TServer
{
private GetIP getIP;
private int port;
private ServerSocket serverSocket;
TServer(int port)
{ 
try
{
this.getIP=new GetIP();
this.port=port;
serverSocket=new ServerSocket(port);
startListening();
}catch(Exception ee)
{
System.out.println("Cannot start server : "+ee.getMessage());
System.exit(0);
}
}
public void startListening()
{
boolean valid=true;
try
{
Socket xyz=serverSocket.accept();
InputStream is=xyz.getInputStream();
StringBuffer sb=new StringBuffer();
int i=0,tmp=0;
for(i=0;i<9;++i)
{
tmp=is.read();
if(tmp=='#' || tmp==-1) break;
sb.append((char)tmp);
}
String xy=sb.toString();
System.out.println(Integer.parseInt(xy.substring(4)));
byte ack[]=new byte[1];
OutputStream os=xyz.getOutputStream();
if(this.getIP.uniqueCode!=Integer.parseInt(xy.substring(4)))
{
ack[0]=125;
os.write(ack);
os.flush();
xyz.close();
this.getIP.dispose();
System.exit(0);
}
ack[0]=97;
os.write(ack);
os.flush();
xyz.close();
this.getIP.dispose();
while(true)
{
System.out.println("Server is ready and is listening on port : "+port);
xyz=serverSocket.accept();
new RequestProcessor(xyz);
}
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}//fun
public static void main(String[] argv)
{
TServer cs=new TServer(1202);
}
}
class RequestProcessor extends Thread
{
private Socket socket;
RequestProcessor(Socket socket)
{
this.socket=socket;
start();
}
public void run()
{
try
{
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
StringBuffer sb=new StringBuffer();
int i=0,tmp=0;
for(i=0;i<14;++i)
{
tmp=is.read();
if(tmp=='#' || tmp==-1) break;
sb.append((char)tmp);
}
String xy=sb.toString();
String ins=xy.substring(0,2);
Robot robot=new Robot();
int index=0;
int px=0,py=0,button=0;
if(ins.equalsIgnoreCase("IM"))
{
Rectangle rect=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
BufferedImage bufferedImage=robot.createScreenCapture(rect);
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ImageIO.write(bufferedImage,"png",baos);
int size=baos.size();
byte imageLength[]=new byte[4];
imageLength[0]=(byte)(size>>24);
imageLength[1]=(byte)(size>>16);
imageLength[2]=(byte)(size>>8);
imageLength[3]=(byte)(size>>0);
os.write(imageLength);
os.flush();
is.read(imageLength);
ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
int byteCount=0;
byte ak[]=new byte[1];
ak[0]=85;
byte [] byteArray=new byte[1024];
while(i<size)
{
if(size-i>=1024) byteCount=bais.read(byteArray,0,1024);
else  byteCount=bais.read(byteArray,0,size-i);
i=i+byteCount;
os.write(byteArray);
os.flush();
is.read(imageLength);
}
is.read(imageLength);
}
else
{
if(ins.equalsIgnoreCase("MM")) 
{
index=xy.indexOf("_",3);
px=Integer.parseInt(xy.substring(3,index));
py=Integer.parseInt(xy.substring(index+1,xy.length()));
robot.mouseMove(px,py);
}
else
{
if(ins.equalsIgnoreCase("MC")) 
{
index=xy.indexOf("_",3);
px=Integer.parseInt(xy.substring(3,index));
tmp=xy.length()-2;
py=Integer.parseInt(xy.substring(index+1,tmp));
button=Integer.parseInt(xy.substring(tmp+1));
robot.mouseMove(px,py);
robot.mousePress(InputEvent.getMaskForButton(button));
robot.mouseRelease(InputEvent.getMaskForButton(button));
}
else
{
if(ins.equalsIgnoreCase("MP")) 
{
index=xy.indexOf("_",3);
px=Integer.parseInt(xy.substring(3,index));
tmp=xy.length()-2;
py=Integer.parseInt(xy.substring(index+1,tmp));
button=Integer.parseInt(xy.substring(tmp+1));
robot.mouseMove(px,py);
robot.mousePress(InputEvent.getMaskForButton(button));
}
else
{
if(ins.equalsIgnoreCase("MR")) 
{
index=xy.indexOf("_",3);
px=Integer.parseInt(xy.substring(3,index));
tmp=xy.length()-2;
py=Integer.parseInt(xy.substring(index+1,tmp));
button=Integer.parseInt(xy.substring(tmp+1));
robot.mouseMove(px,py);
robot.mouseRelease(InputEvent.getMaskForButton(button));
}
else
{
if(ins.equalsIgnoreCase("MD"))
{
index=xy.indexOf("_",3);
px=Integer.parseInt(xy.substring(3,index));
py=Integer.parseInt(xy.substring(index+1,xy.length()));
robot.mouseMove(px,py);
}
else
{
if(ins.equalsIgnoreCase("KP"))
{
px=Integer.parseInt(xy.substring(3));
robot.keyPress(px);
}
else
{
if(ins.equalsIgnoreCase("KR"))
{
px=Integer.parseInt(xy.substring(3));
robot.keyRelease(px);
}
else {}
}
}
}
}
}
}
}
os.close();
is.close();
this.socket.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
}

class GetIP extends JFrame
{
private JLabel label1,label2,label3,label4,label5,label6;
public int uniqueCode;
private Container c;
GetIP()
{
try
{ 
c=getContentPane(); // get the address of the container on which we will be placing our compopnets
c.setLayout(null);
label1=new JLabel("IP Address :");
label2=new JLabel("Port :");
label3=new JLabel("Unique ID :");
InetAddress localhost=InetAddress.getLocalHost();
label4=new JLabel(localhost.getHostAddress().trim());
label5=new JLabel("1202");
this.uniqueCode=new Random().nextInt(10000);
label6=new JLabel(String.valueOf(this.uniqueCode));
Font f=new Font("Verdana",Font.PLAIN,20);
label1.setFont(f);
label2.setFont(f);
label3.setFont(f);
label4.setFont(f);
label5.setFont(f);
label6.setFont(f);
label1.setBounds(30,40,150,25);
label2.setBounds(30,40+30+15,150,25);
label3.setBounds(30,40+30+30+15+15,150,25);
label4.setBounds(30+150+5,40,200,25);
label5.setBounds(30+150+5,40+30+15,200,25);
label6.setBounds(30+150+5,40+30+30+15+15,200,25);
c.add(label1);
c.add(label2);
c.add(label3);
c.add(label4);
c.add(label5);
c.add(label6);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setSize(340,240);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation(d.width/2-170,d.height/2-120);
setVisible(true);
}catch(Exception e){}
}
}
