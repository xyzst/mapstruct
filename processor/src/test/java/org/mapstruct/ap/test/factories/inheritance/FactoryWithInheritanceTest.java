/**
 *  Copyright 2012-2015 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.test.factories.inheritance;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.runner.AnnotationProcessorTestRunner;

/**
 *
 * @author Sjaak Derksen
 */

@IssueKey("385")
@WithClasses( { BoatDto.class, BoatEntity.class, PowerBoatDto.class, PowerBoatEntity.class, BoatMapper.class,
    EntityFactory.class } )

@RunWith(AnnotationProcessorTestRunner.class)
public class FactoryWithInheritanceTest {

    @Test
    public void shouldNotGenerateAmbigousFactoryMethod() {

        // check boat, should be initialized to null (crew)
        BoatDto boatDto = new BoatDto();
        boatDto.setPowerTrain( "diesel" );

        BoatEntity boatEntity = BoatMapper.INSTANCE.mapToEntity( boatDto );
        assertThat( boatEntity ).isNotNull();
        assertThat( boatEntity.getPowerTrain() ).isEqualTo( "diesel" );
        assertThat( boatEntity.getNoOfCrew() ).isEqualTo( null );

        // check boat, should be initialized to 2 (crew)
        PowerBoatDto powerBoatDto = new PowerBoatDto();
        powerBoatDto.setPowerTrain( "gas" );

        PowerBoatEntity powerBoatEntity = BoatMapper.INSTANCE.mapToEntity( powerBoatDto );
        assertThat( powerBoatEntity ).isNotNull();
        assertThat( powerBoatEntity.getPowerTrain() ).isEqualTo( "gas" );
        assertThat( powerBoatEntity.getNoOfCrew() ).isEqualTo( 2 );
    }

}
