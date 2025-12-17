with
  production_per_supplier_and_material as (
    select
      snow_globe_materials.material
      , supplier_inventory.qty / snow_globe_materials.qty as production_possible
      , supplier_inventory.supplier
    from snow_globe_materials
    left join supplier_inventory
      using (material)
    group by snow_globe_materials.material, supplier_inventory.supplier
  )

  , production_per_supplier as (
    select
      supplier
      , min(production_possible) as actual_production
    from production_per_supplier_and_material
    group by supplier
  )

select
  supplier
from production_per_supplier
order by actual_production desc, supplier
limit 1
