/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.catalyst.analysis

import org.apache.paimon.spark.commands.DeleteFromPaimonTableCommand

import org.apache.spark.sql.catalyst.plans.logical.{DeleteFromTable, LogicalPlan}
import org.apache.spark.sql.catalyst.rules.Rule

object PaimonDeleteTable extends Rule[LogicalPlan] with RowLevelHelper {

  override val operation: RowLevelOp = Delete

  override def apply(plan: LogicalPlan): LogicalPlan = {
    plan.resolveOperators {
      case d @ DeleteFromTable(PaimonRelation(table), condition) if d.resolved =>
        checkPaimonTable(table)

        DeleteFromPaimonTableCommand(table, d)
    }
  }
}
