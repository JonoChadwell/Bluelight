package bluelight;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LightController {
    private List<Thread> threads = new ArrayList<>();
    
    private static interface LightLoop {
        public void loop() throws Exception;
    }
    
    private final BluetoothConnection connection;
    
    public LightController(BluetoothConnection connection) {
        this.connection = connection;
    }
    
    private void run(LightLoop task) {
    	for (Thread t : threads) {
			System.out.println("Killing: " + t.getName());
    	}
        final List<Thread> toWait = new ArrayList<>();
        Iterator<Thread> iter = threads.iterator();
        while (iter.hasNext()) {
            Thread t = iter.next();
            if (t.isAlive()) {
                t.interrupt();
                toWait.add(t);
            } else {
                iter.remove();
            }
        }
        final Thread run = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    task.loop();
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                	System.out.println("Stopping Interrupted");
                    Thread.currentThread().interrupt();
                } catch (Exception ex) {
                    System.out.println("Task threw exception: " + ex.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " is no more");
        });
        Thread startRunWhenAlone = new Thread(() -> {
            for (Thread t : toWait) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    return;
                }
            }
            run.start();
            System.out.println(Thread.currentThread().getName() + " is no more");
        });
        threads.add(run);
        threads.add(startRunWhenAlone);
        startRunWhenAlone.start();
    }
    
    private int position;
    public void fade() {
        run(() -> {
        	position++;
            position %= 1535;
            if (position < 512) {
                connection.setLights(convertRange(position), 0, 0);
            } else if (position < 1024) {
                connection.setLights(0, convertRange(position), 0);
            } else {
                connection.setLights(0, 0, convertRange(position));
            }
        });
    }
    
    private static int convertRange(int val) {
        return (val & 0x1FF) < 256 ? val & 0xFF : ~val & 0xFF;
    }
    
    int colors[] = {255,0,0};
    public void rainbow() {
        run(() -> {
            for (int i = 3; i < 6; i++) {
                if (colors[i % 3] == 255) {
                    if (colors[(i - 1) % 3] > 0) {
                        colors[(i - 1) % 3] = colors[(i - 1) % 3] - 1;
                    } else if (colors[(i + 1) % 3] < 255) {
                        colors[(i + 1) % 3] = colors[(i + 1) % 3] + 1;
                    } else {
                        colors[i % 3] = 254;
                        break;
                    }
                }
            }
            connection.setLights(colors[0], colors[1], colors[2]);
        });
    }
    
    public void stop() {
        run(() -> {
            connection.setLights(0,0,0);
            Thread.currentThread().interrupt();
        });
    }
    
    public void setLights(int r, int g, int b) {
    	run(() -> {
    		connection.setLights(r, g, b);
    		Thread.currentThread().interrupt();
    	});
    }
    
    public void setLights(int r, int g, int b, int p, int s) {
    	run(() -> {
    		connection.setLights(r, g, b, p, s);
    		Thread.currentThread().interrupt();
    	});
    }
    
    private int randomByte() {
		return (int) (Math.random() * 255);
    }

	public void randomize(boolean strobe) {
		run(() -> {
    		connection.setLights(randomByte(), randomByte(), randomByte(), randomByte() | 0x80, strobe ? randomByte() : 0);;
    		Thread.currentThread().interrupt();
    	});
	}
	
	public void randomizeRepeatFast() {
		run(() -> {
    		connection.setLights(randomByte(), randomByte(), randomByte(), 255, 0);
    	});
	}
	
	public void randomizeRepeatSlow() {
		run(() -> {
    		connection.setLights(randomByte(), randomByte(), randomByte(), randomByte() | 0x80, 0);
    		Thread.sleep(500);
		});
	}
	
	boolean toggle;
	public void internet() {
		run(() -> {
			if (toggle) {
				connection.setLights(255, 0, 0, 0, 0);
				Thread.sleep(200);
			} else {
				connection.setLights(0, 0, 0, 0, 0);
				Thread.sleep(1200);
			}
			toggle = !toggle;
		});
	}
}