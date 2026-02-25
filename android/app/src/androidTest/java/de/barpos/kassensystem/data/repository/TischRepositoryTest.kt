package de.barpos.kassensystem.data.repository

import org.junit.Test

class TischRepositoryTest {

    @Test(expected = Exception::class)
    fun `version conflict throws OptimisticLockException`() {
        // This test must fail first
        assert(false)
    }
}
