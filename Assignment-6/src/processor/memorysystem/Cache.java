package processor.memorysystem;
import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.pipeline.MemoryAccess;

public class Cache implements Element {
    Processor containingProcessor;
    int cacheSize;
    int cacheLatency;
    CacheLine[] actualCache;
    int noOfLines;
    int noOfSets;
    int cacheMissAddress;
    Element cacheMissElement;
    Boolean isRead;
    int writeData;

    public Cache(Processor processor, int size){
        this.containingProcessor = processor;
        this.cacheSize = size;
        this.noOfLines = size / 4;
        this.noOfSets = noOfLines / 2;

        switch(size){
            case 8:
                cacheLatency = 1;
                break;
            case 32:
                cacheLatency = 2;
                break;
            case 128:
                cacheLatency = 4;
                break;
            case 1024:
                cacheLatency = 8;
                break;
        }
        actualCache = new CacheLine[noOfLines];
        for(int i = 0; i < noOfLines; i++)
            actualCache[i] = new CacheLine();
    }

    public int getCacheLatency() { return cacheLatency; }

    public static String toBinary(int x, int len){
        if (len > 0) {
            return String.format("%" + len + "s",
                    Integer.toBinaryString(x)).replace(" ", "0");
        }
        return null;
    }

    public void handleCacheMiss(int address, Element requestingElement){
        Simulator.getEventQueue().addEvent(
                new MemoryReadEvent(
                        Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        address
                )
        );
        cacheMissAddress = address;
        cacheMissElement = requestingElement;
    }

    public void handleResponse(int value){
        String addressString = toBinary(cacheMissAddress, 32);
        int indexBits = (int) (Math.log(noOfLines) / Math.log(2));
        int cacheAddress;
        if(indexBits == 0){
            cacheAddress = 0;
        }
        else{
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        }
        actualCache[cacheAddress].setData(value);
        actualCache[cacheAddress].setTag(cacheMissAddress);
        if(isRead){
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            cacheMissElement,
                            value
                    )
            );
        }
        else{
            cacheWrite(cacheMissAddress, writeData, cacheMissElement);
        }
    }

    public void cacheRead(int address, Element requestingElement){
        String addressString = toBinary(address, 32);
        int cacheAddress;
        int indexBits = (int) (Math.log(noOfLines) / Math.log(2));
        if(indexBits == 0) {
            cacheAddress = 0;
        }
        else{
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        }
        int cacheTag = actualCache[cacheAddress].getTag();
        if(cacheTag == address){
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            requestingElement,
                            actualCache[cacheAddress].getData()
                    )
            );
        }
        else{
            isRead = true;
            handleCacheMiss(address, requestingElement);
        }
    }

    public void cacheWrite(int address, int value, Element requestingElement){
        String addressString = toBinary(address, 32);
        int cacheAddress;
        int indexBits = (int) (Math.log(noOfLines) / Math.log(2));
        if(indexBits == 0){
            cacheAddress = 0;
        }
        else{
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        }
        int cacheTag = actualCache[cacheAddress].getTag();
        if(cacheTag == address){
            actualCache[cacheAddress].setData(value);
            Simulator.getEventQueue().addEvent(
                    new MemoryWriteEvent(
                            Clock.getCurrentTime(),
                            this,
                            containingProcessor.getMainMemory(),
                            address,
                            value
                    )
            );
            ((MemoryAccess)requestingElement).EX_MA_Latch.setMA_Busy(false);
            ((MemoryAccess)requestingElement).MA_RW_Latch.setRW_enable(true);
        }
        else{
            isRead = false;
            writeData = value;
            handleCacheMiss(address, requestingElement);
        }
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == Event.EventType.MemoryRead){
            MemoryReadEvent event = (MemoryReadEvent) e;
            cacheRead(event.getAddressToReadFrom(), event.getRequestingElement());
        }
        else if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event.getValue());
        }
        else if(e.getEventType() == Event.EventType.MemoryWrite){
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            cacheWrite(event.getAddressToWriteTo(), event.getValue(), event.getRequestingElement());
        }
    }
}
