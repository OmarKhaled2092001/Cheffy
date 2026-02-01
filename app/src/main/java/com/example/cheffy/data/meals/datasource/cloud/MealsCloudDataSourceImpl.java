package com.example.cheffy.data.meals.datasource.cloud;

import android.util.Log;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.models.local.MealPlanEntity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealsCloudDataSourceImpl implements IMealsCloudDataSource {

    private static final String TAG = "MealsCloudDataSource";
    private static final String COLLECTION_USERS = "users";
    private static final String SUBCOLLECTION_FAVORITES = "favorites";
    private static final String SUBCOLLECTION_MEAL_PLANS = "meal_plans";

    private final FirebaseFirestore firestore;
    private static MealsCloudDataSourceImpl instance;

    private MealsCloudDataSourceImpl() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized MealsCloudDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new MealsCloudDataSourceImpl();
        }
        return instance;
    }

    @Override
    public Completable uploadFavorite(String uid, FavoriteMealEntity meal) {
        return Completable.create(emitter -> {
            Map<String, Object> data = favoriteToMap(meal);
            
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_FAVORITES)
                    .document(meal.getIdMeal())
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    @Override
    public Completable deleteFavorite(String uid, String mealId) {
        return Completable.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_FAVORITES)
                    .document(mealId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    @Override
    public Completable uploadMealPlan(String uid, MealPlanEntity plan) {
        return Completable.create(emitter -> {
            Map<String, Object> data = mealPlanToMap(plan);
            String documentId = plan.getIdMeal() + "_" + plan.getDayOfWeek();
            
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_MEAL_PLANS)
                    .document(documentId)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    @Override
    public Completable deleteMealPlan(String uid, String mealId, String dayOfWeek) {
        return Completable.create(emitter -> {
            String documentId = mealId + "_" + dayOfWeek;
            
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_MEAL_PLANS)
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }


    @Override
    public Single<List<FavoriteMealEntity>> fetchAllFavorites(String uid) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_FAVORITES)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<FavoriteMealEntity> favorites = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            FavoriteMealEntity entity = mapToFavorite(doc);
                            if (entity != null) {
                                favorites.add(entity);
                            }
                        }
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(favorites);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    @Override
    public Single<List<MealPlanEntity>> fetchAllMealPlans(String uid) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_MEAL_PLANS)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<MealPlanEntity> plans = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            MealPlanEntity entity = mapToMealPlan(doc);
                            if (entity != null) {
                                plans.add(entity);
                            }
                        }
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(plans);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }


    @Override
    public Completable uploadAllFavorites(String uid, List<FavoriteMealEntity> meals) {
        if (meals == null || meals.isEmpty()) {
            return Completable.complete();
        }

        return Completable.create(emitter -> {
            WriteBatch batch = firestore.batch();
            
            for (FavoriteMealEntity meal : meals) {
                Map<String, Object> data = favoriteToMap(meal);
                batch.set(
                        firestore.collection(COLLECTION_USERS)
                                .document(uid)
                                .collection(SUBCOLLECTION_FAVORITES)
                                .document(meal.getIdMeal()),
                        data,
                        SetOptions.merge()
                );
            }
            
            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    @Override
    public Completable uploadAllMealPlans(String uid, List<MealPlanEntity> plans) {
        if (plans == null || plans.isEmpty()) {
            return Completable.complete();
        }

        return Completable.create(emitter -> {
            WriteBatch batch = firestore.batch();
            
            for (MealPlanEntity plan : plans) {
                Map<String, Object> data = mealPlanToMap(plan);
                String documentId = plan.getIdMeal() + "_" + plan.getDayOfWeek();
                batch.set(
                        firestore.collection(COLLECTION_USERS)
                                .document(uid)
                                .collection(SUBCOLLECTION_MEAL_PLANS)
                                .document(documentId),
                        data,
                        SetOptions.merge()
                );
            }
            
            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }


    @Override
    public Single<Boolean> hasCloudData(String uid) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(uid)
                    .collection(SUBCOLLECTION_FAVORITES)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        boolean hasData = !querySnapshot.isEmpty();
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(hasData);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }


    private Map<String, Object> favoriteToMap(FavoriteMealEntity meal) {
        Map<String, Object> map = new HashMap<>();
        map.put("idMeal", meal.getIdMeal());
        map.put("name", meal.getName());
        map.put("category", meal.getCategory());
        map.put("area", meal.getArea());
        map.put("instructions", meal.getInstructions());
        map.put("thumbnail", meal.getThumbnail());
        map.put("youtube", meal.getYoutube());
        map.put("tags", meal.getTags());
        
        map.put("ingredient1", meal.getIngredient1());
        map.put("ingredient2", meal.getIngredient2());
        map.put("ingredient3", meal.getIngredient3());
        map.put("ingredient4", meal.getIngredient4());
        map.put("ingredient5", meal.getIngredient5());
        map.put("ingredient6", meal.getIngredient6());
        map.put("ingredient7", meal.getIngredient7());
        map.put("ingredient8", meal.getIngredient8());
        map.put("ingredient9", meal.getIngredient9());
        map.put("ingredient10", meal.getIngredient10());
        map.put("ingredient11", meal.getIngredient11());
        map.put("ingredient12", meal.getIngredient12());
        map.put("ingredient13", meal.getIngredient13());
        map.put("ingredient14", meal.getIngredient14());
        map.put("ingredient15", meal.getIngredient15());
        map.put("ingredient16", meal.getIngredient16());
        map.put("ingredient17", meal.getIngredient17());
        map.put("ingredient18", meal.getIngredient18());
        map.put("ingredient19", meal.getIngredient19());
        map.put("ingredient20", meal.getIngredient20());
        

        map.put("measure1", meal.getMeasure1());
        map.put("measure2", meal.getMeasure2());
        map.put("measure3", meal.getMeasure3());
        map.put("measure4", meal.getMeasure4());
        map.put("measure5", meal.getMeasure5());
        map.put("measure6", meal.getMeasure6());
        map.put("measure7", meal.getMeasure7());
        map.put("measure8", meal.getMeasure8());
        map.put("measure9", meal.getMeasure9());
        map.put("measure10", meal.getMeasure10());
        map.put("measure11", meal.getMeasure11());
        map.put("measure12", meal.getMeasure12());
        map.put("measure13", meal.getMeasure13());
        map.put("measure14", meal.getMeasure14());
        map.put("measure15", meal.getMeasure15());
        map.put("measure16", meal.getMeasure16());
        map.put("measure17", meal.getMeasure17());
        map.put("measure18", meal.getMeasure18());
        map.put("measure19", meal.getMeasure19());
        map.put("measure20", meal.getMeasure20());
        
        map.put("addedAt", meal.getAddedAt());
        
        return map;
    }

    private Map<String, Object> mealPlanToMap(MealPlanEntity plan) {
        Map<String, Object> map = new HashMap<>();
        map.put("idMeal", plan.getIdMeal());
        map.put("name", plan.getName());
        map.put("category", plan.getCategory());
        map.put("area", plan.getArea());
        map.put("instructions", plan.getInstructions());
        map.put("thumbnail", plan.getThumbnail());
        map.put("youtube", plan.getYoutube());
        map.put("tags", plan.getTags());
        map.put("dayOfWeek", plan.getDayOfWeek());
        
        map.put("ingredient1", plan.getIngredient1());
        map.put("ingredient2", plan.getIngredient2());
        map.put("ingredient3", plan.getIngredient3());
        map.put("ingredient4", plan.getIngredient4());
        map.put("ingredient5", plan.getIngredient5());
        map.put("ingredient6", plan.getIngredient6());
        map.put("ingredient7", plan.getIngredient7());
        map.put("ingredient8", plan.getIngredient8());
        map.put("ingredient9", plan.getIngredient9());
        map.put("ingredient10", plan.getIngredient10());
        map.put("ingredient11", plan.getIngredient11());
        map.put("ingredient12", plan.getIngredient12());
        map.put("ingredient13", plan.getIngredient13());
        map.put("ingredient14", plan.getIngredient14());
        map.put("ingredient15", plan.getIngredient15());
        map.put("ingredient16", plan.getIngredient16());
        map.put("ingredient17", plan.getIngredient17());
        map.put("ingredient18", plan.getIngredient18());
        map.put("ingredient19", plan.getIngredient19());
        map.put("ingredient20", plan.getIngredient20());
        

        map.put("measure1", plan.getMeasure1());
        map.put("measure2", plan.getMeasure2());
        map.put("measure3", plan.getMeasure3());
        map.put("measure4", plan.getMeasure4());
        map.put("measure5", plan.getMeasure5());
        map.put("measure6", plan.getMeasure6());
        map.put("measure7", plan.getMeasure7());
        map.put("measure8", plan.getMeasure8());
        map.put("measure9", plan.getMeasure9());
        map.put("measure10", plan.getMeasure10());
        map.put("measure11", plan.getMeasure11());
        map.put("measure12", plan.getMeasure12());
        map.put("measure13", plan.getMeasure13());
        map.put("measure14", plan.getMeasure14());
        map.put("measure15", plan.getMeasure15());
        map.put("measure16", plan.getMeasure16());
        map.put("measure17", plan.getMeasure17());
        map.put("measure18", plan.getMeasure18());
        map.put("measure19", plan.getMeasure19());
        map.put("measure20", plan.getMeasure20());
        
        map.put("addedAt", plan.getAddedAt());
        
        return map;
    }

    private FavoriteMealEntity mapToFavorite(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) {
            return null;
        }
        
        FavoriteMealEntity entity = new FavoriteMealEntity();
        entity.setIdMealPk(getStringOrDefault(doc, "idMeal", ""));
        entity.setName(getStringOrDefault(doc, "name", ""));
        entity.setCategory(getStringOrDefault(doc, "category", ""));
        entity.setArea(getStringOrDefault(doc, "area", ""));
        entity.setInstructions(getStringOrDefault(doc, "instructions", ""));
        entity.setThumbnail(getStringOrDefault(doc, "thumbnail", ""));
        entity.setYoutube(getStringOrDefault(doc, "youtube", ""));
        entity.setTags(getStringOrDefault(doc, "tags", ""));
        
        entity.setIngredient1(getStringOrDefault(doc, "ingredient1", null));
        entity.setIngredient2(getStringOrDefault(doc, "ingredient2", null));
        entity.setIngredient3(getStringOrDefault(doc, "ingredient3", null));
        entity.setIngredient4(getStringOrDefault(doc, "ingredient4", null));
        entity.setIngredient5(getStringOrDefault(doc, "ingredient5", null));
        entity.setIngredient6(getStringOrDefault(doc, "ingredient6", null));
        entity.setIngredient7(getStringOrDefault(doc, "ingredient7", null));
        entity.setIngredient8(getStringOrDefault(doc, "ingredient8", null));
        entity.setIngredient9(getStringOrDefault(doc, "ingredient9", null));
        entity.setIngredient10(getStringOrDefault(doc, "ingredient10", null));
        entity.setIngredient11(getStringOrDefault(doc, "ingredient11", null));
        entity.setIngredient12(getStringOrDefault(doc, "ingredient12", null));
        entity.setIngredient13(getStringOrDefault(doc, "ingredient13", null));
        entity.setIngredient14(getStringOrDefault(doc, "ingredient14", null));
        entity.setIngredient15(getStringOrDefault(doc, "ingredient15", null));
        entity.setIngredient16(getStringOrDefault(doc, "ingredient16", null));
        entity.setIngredient17(getStringOrDefault(doc, "ingredient17", null));
        entity.setIngredient18(getStringOrDefault(doc, "ingredient18", null));
        entity.setIngredient19(getStringOrDefault(doc, "ingredient19", null));
        entity.setIngredient20(getStringOrDefault(doc, "ingredient20", null));
        
        entity.setMeasure1(getStringOrDefault(doc, "measure1", null));
        entity.setMeasure2(getStringOrDefault(doc, "measure2", null));
        entity.setMeasure3(getStringOrDefault(doc, "measure3", null));
        entity.setMeasure4(getStringOrDefault(doc, "measure4", null));
        entity.setMeasure5(getStringOrDefault(doc, "measure5", null));
        entity.setMeasure6(getStringOrDefault(doc, "measure6", null));
        entity.setMeasure7(getStringOrDefault(doc, "measure7", null));
        entity.setMeasure8(getStringOrDefault(doc, "measure8", null));
        entity.setMeasure9(getStringOrDefault(doc, "measure9", null));
        entity.setMeasure10(getStringOrDefault(doc, "measure10", null));
        entity.setMeasure11(getStringOrDefault(doc, "measure11", null));
        entity.setMeasure12(getStringOrDefault(doc, "measure12", null));
        entity.setMeasure13(getStringOrDefault(doc, "measure13", null));
        entity.setMeasure14(getStringOrDefault(doc, "measure14", null));
        entity.setMeasure15(getStringOrDefault(doc, "measure15", null));
        entity.setMeasure16(getStringOrDefault(doc, "measure16", null));
        entity.setMeasure17(getStringOrDefault(doc, "measure17", null));
        entity.setMeasure18(getStringOrDefault(doc, "measure18", null));
        entity.setMeasure19(getStringOrDefault(doc, "measure19", null));
        entity.setMeasure20(getStringOrDefault(doc, "measure20", null));
        
        Long addedAt = doc.getLong("addedAt");
        entity.setAddedAt(addedAt != null ? addedAt : System.currentTimeMillis());
        
        return entity;
    }

    private MealPlanEntity mapToMealPlan(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) {
            return null;
        }
        
        MealPlanEntity entity = new MealPlanEntity();
        entity.setIdMealField(getStringOrDefault(doc, "idMeal", ""));
        entity.setName(getStringOrDefault(doc, "name", ""));
        entity.setCategory(getStringOrDefault(doc, "category", ""));
        entity.setArea(getStringOrDefault(doc, "area", ""));
        entity.setInstructions(getStringOrDefault(doc, "instructions", ""));
        entity.setThumbnail(getStringOrDefault(doc, "thumbnail", ""));
        entity.setYoutube(getStringOrDefault(doc, "youtube", ""));
        entity.setTags(getStringOrDefault(doc, "tags", ""));
        entity.setDayOfWeek(getStringOrDefault(doc, "dayOfWeek", ""));
        
        entity.setIngredient1(getStringOrDefault(doc, "ingredient1", null));
        entity.setIngredient2(getStringOrDefault(doc, "ingredient2", null));
        entity.setIngredient3(getStringOrDefault(doc, "ingredient3", null));
        entity.setIngredient4(getStringOrDefault(doc, "ingredient4", null));
        entity.setIngredient5(getStringOrDefault(doc, "ingredient5", null));
        entity.setIngredient6(getStringOrDefault(doc, "ingredient6", null));
        entity.setIngredient7(getStringOrDefault(doc, "ingredient7", null));
        entity.setIngredient8(getStringOrDefault(doc, "ingredient8", null));
        entity.setIngredient9(getStringOrDefault(doc, "ingredient9", null));
        entity.setIngredient10(getStringOrDefault(doc, "ingredient10", null));
        entity.setIngredient11(getStringOrDefault(doc, "ingredient11", null));
        entity.setIngredient12(getStringOrDefault(doc, "ingredient12", null));
        entity.setIngredient13(getStringOrDefault(doc, "ingredient13", null));
        entity.setIngredient14(getStringOrDefault(doc, "ingredient14", null));
        entity.setIngredient15(getStringOrDefault(doc, "ingredient15", null));
        entity.setIngredient16(getStringOrDefault(doc, "ingredient16", null));
        entity.setIngredient17(getStringOrDefault(doc, "ingredient17", null));
        entity.setIngredient18(getStringOrDefault(doc, "ingredient18", null));
        entity.setIngredient19(getStringOrDefault(doc, "ingredient19", null));
        entity.setIngredient20(getStringOrDefault(doc, "ingredient20", null));
        
        entity.setMeasure1(getStringOrDefault(doc, "measure1", null));
        entity.setMeasure2(getStringOrDefault(doc, "measure2", null));
        entity.setMeasure3(getStringOrDefault(doc, "measure3", null));
        entity.setMeasure4(getStringOrDefault(doc, "measure4", null));
        entity.setMeasure5(getStringOrDefault(doc, "measure5", null));
        entity.setMeasure6(getStringOrDefault(doc, "measure6", null));
        entity.setMeasure7(getStringOrDefault(doc, "measure7", null));
        entity.setMeasure8(getStringOrDefault(doc, "measure8", null));
        entity.setMeasure9(getStringOrDefault(doc, "measure9", null));
        entity.setMeasure10(getStringOrDefault(doc, "measure10", null));
        entity.setMeasure11(getStringOrDefault(doc, "measure11", null));
        entity.setMeasure12(getStringOrDefault(doc, "measure12", null));
        entity.setMeasure13(getStringOrDefault(doc, "measure13", null));
        entity.setMeasure14(getStringOrDefault(doc, "measure14", null));
        entity.setMeasure15(getStringOrDefault(doc, "measure15", null));
        entity.setMeasure16(getStringOrDefault(doc, "measure16", null));
        entity.setMeasure17(getStringOrDefault(doc, "measure17", null));
        entity.setMeasure18(getStringOrDefault(doc, "measure18", null));
        entity.setMeasure19(getStringOrDefault(doc, "measure19", null));
        entity.setMeasure20(getStringOrDefault(doc, "measure20", null));
        
        Long addedAt = doc.getLong("addedAt");
        entity.setAddedAt(addedAt != null ? addedAt : System.currentTimeMillis());
        
        return entity;
    }

    private String getStringOrDefault(DocumentSnapshot doc, String field, String defaultValue) {
        String value = doc.getString(field);
        return value != null ? value : defaultValue;
    }
}
