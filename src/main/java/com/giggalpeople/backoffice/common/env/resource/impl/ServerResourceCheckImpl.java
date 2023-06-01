package com.giggalpeople.backoffice.common.env.resource.impl;

import com.giggalpeople.backoffice.common.env.exception.ServerInfoException;
import com.giggalpeople.backoffice.common.env.resource.ServerResourceCheck;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

public class ServerResourceCheckImpl implements ServerResourceCheck {

    /**
     * <b>외부에서 Server 자원 정보 조회 시 호출하여 모든 정보를 가져오는 Method</b>
     * @param responseCount 초당 조회 횟수
     * @return Server 자원 관련 조회 정보(CPU, Memory, Disk)
     */

    @Override
    public List<String> getServerResourcesInfo(int countPerMilliSecond, int responseCount) {
        List<String> resultList = new ArrayList<>();
        if (countPerMilliSecond > 1 && responseCount > 1) {
            resultList.add(countPerMilliSecond + "ms 당 " + responseCount + "번 서버 자원 조회 Report \n");

            for (int index = 0; index < responseCount; index++) {
                resultList.add((index + 1) + "번째 자원 사용 정보 \n\n");
                resultList.add(getCPUProcessInfo());
                resultList.add(getMemoryInfo().toString());
                resultList.add(getDiskSpaceInfo().toString());

                try {
                    Thread.sleep(countPerMilliSecond);
                } catch (InterruptedException interruptedException) {
                    throw new ServerInfoException(interruptedException.getMessage(), interruptedException);
                }
            }

        } else if (countPerMilliSecond < 1 && responseCount > 1) {
            resultList.add(responseCount + "번 서버 자원 조회 Report \n");

            for (int index = 0; index < responseCount; index++) {
                resultList.add((index + 1) + "번째 자원 사용 정보 \n\n");
                resultList.add(getCPUProcessInfo());
                resultList.add(getMemoryInfo().toString());
                resultList.add(getDiskSpaceInfo().toString());
            }

        } else {
            resultList.add(getCPUProcessInfo());
            resultList.add(getMemoryInfo().toString());
            resultList.add(getDiskSpaceInfo().toString());
        }
        return resultList;
    }

    /**
     * <b>Application Disk 용량 정보 가져오기 위한 Method</b>
     *
     * @return Disk 총 용량과 사용량 정보를 담은 List
     */
    @Override
    public StringBuilder getDiskSpaceInfo() {
        File file = new File("/");
        StringBuilder result = new StringBuilder();

        result.append("Disk 총 공간 정보 : ")
                .append(processDiskSize(file.getTotalSpace()))
                .append(" MB")
                .append("\n")
                .append("Disk 사용량 정보 : ")
                .append(processDiskSize(file.getUsableSpace()))
                .append(" MB")
                .append("\n\n");

        return result;
    }

    /**
     * <b>Application CPU 사용량 정보 가져오기 위한 Method</b>
     *
     * @return CPU 사용량 정보를 담은 문자열
     */
    @Override
    public String getCPUProcessInfo() {
        String result;
        result = "CPU 자원 정보 : ";
        result += String.format("%.2f", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() * 100);
        result += "\n";
        return result;
    }

    /**
     * <b>Application Memory 사용량 정보 가져오기 위한 Method</b>
     *
     * @return Heap Memory, Non Heap Memory, 총 Memory 중 사용 가능 Memory 용량을 뺀 정보를 담은 List
     */

    @Override
    public StringBuilder getMemoryInfo() {
        StringBuilder result = new StringBuilder();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

        result.append("초기 상태 Memory 정보 : ")
                .append(heapMemoryUsage.getInit() + " Bytes")
                .append("\n")
                .append("사용 중 Memory 정보 : ")
                .append(heapMemoryUsage.getUsed() + " Bytes")
                .append("\n")
                .append("JVM 할당 Memory 정보 : ")
                .append(heapMemoryUsage.getCommitted() + " Bytes")
                .append("\n")
                .append("Memory 총 용량 정보 : ")
                .append(heapMemoryUsage.getMax() + " Bytes")
                .append("\n")
                .append("Heap Memory가 아닌 Memory 사용량 정보 : ")
                .append("\n")
                .append(memoryMXBean.getNonHeapMemoryUsage().getUsed() + " Bytes")
                .append("\n")
                .append("Runtime.getRuntime()을 이용한 총 용량 - 사용 가능 용량을 연산하여 얻은 Memory 사용량 정보 : ")
                .append((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " Bytes")
                .append("\n\n");

        return result;
    }

    private String processDiskSize(long diskSize) {
        return String.valueOf((int) (diskSize / 1024 * 1024));
    }
}
