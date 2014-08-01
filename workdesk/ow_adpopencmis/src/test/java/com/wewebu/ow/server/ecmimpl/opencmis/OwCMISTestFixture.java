package com.wewebu.ow.server.ecmimpl.opencmis;

public interface OwCMISTestFixture
{
    public static final OwCMISTestFixture NO_FIXTURE = new OwCMISTestFixture() {

        @Override
        public void tearDown() throws Exception
        {
            //void
        }

        @Override
        public void setUp() throws Exception
        {
            //void
        }
    };

    void setUp() throws Exception;

    void tearDown() throws Exception;
}
