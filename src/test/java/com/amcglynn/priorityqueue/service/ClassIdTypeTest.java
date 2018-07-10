package com.amcglynn.priorityqueue.service;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassIdTypeTest {

    @Test
    public void testGetIdClassForNormalId() {
        assertThat(ClassIdType.fromValue(1L)).isEqualTo(ClassIdType.NORMAL);
    }

    @Test
    public void testGetIdClassForPriorityId() {
        assertThat(ClassIdType.fromValue(3L)).isEqualTo(ClassIdType.PRIORITY);
    }

    @Test
    public void testGetIdClassForVipId() {
        assertThat(ClassIdType.fromValue(5L)).isEqualTo(ClassIdType.VIP);
    }

    @Test
    public void testGetIdClassForManagementOverrideId() {
        assertThat(ClassIdType.fromValue(15L)).isEqualTo(ClassIdType.MANAGEMENT_OVERRIDE);
    }
}
