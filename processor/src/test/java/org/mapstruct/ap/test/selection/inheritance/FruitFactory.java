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
package org.mapstruct.ap.test.selection.inheritance;

/**
 *
 * @author Sjaak Derksen
 */
public class FruitFactory {

    public Fruit createFruit() {
        return new Fruit( "fruit" );
    }

    public FruitDto createFruitDto() {
        return new FruitDto( "fruit" );
    }

    public Apple createApple() {
        return new Apple( "apple" );
    }

    public AppleDto createApppleDto() {
        return new AppleDto( "apple" );
    }

    public GoldenDelicious createGoldenDelicious() {
        return new GoldenDelicious( "goldenDelicious" );
    }

    public GoldenDeliciousDto createGoldenDeliciousDto() {
        return new GoldenDeliciousDto( "goldenDelicious" );
    }


}
